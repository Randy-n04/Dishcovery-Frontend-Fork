package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.domain.repository.ProfileRepository
import com.dishcovery.app.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _popularRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val popularRecipes: StateFlow<List<Recipe>> = _popularRecipes.asStateFlow()

    private val _savedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val savedRecipes: StateFlow<List<Recipe>> = _savedRecipes.asStateFlow()

    private val _kitchenCategories = MutableStateFlow<List<KitchenCategory>>(emptyList())
    val kitchenCategories: StateFlow<List<KitchenCategory>> = _kitchenCategories.asStateFlow()

    private val _aiTip = MutableStateFlow<String?>(null)
    val aiTip: StateFlow<String?> = _aiTip.asStateFlow()

    private val _recommendedRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recommendedRecipes: StateFlow<List<Recipe>> = _recommendedRecipes.asStateFlow()

    private val _feedMessage = MutableStateFlow<String?>(null)
    val feedMessage: StateFlow<String?> = _feedMessage.asStateFlow()

    private val _homeError = MutableStateFlow<String?>(null)
    val homeError: StateFlow<String?> = _homeError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isFeedLoading = MutableStateFlow(false)
    val isFeedLoading: StateFlow<Boolean> = _isFeedLoading.asStateFlow()

    private var currentProfileId: Long? = null
    private var loadedUserId: Long? = null

    fun loadHomeData(userId: Long?) {
        if (userId == null) {
            _homeError.value = "No se encontró el usuario actual."
            return
        }

        if (loadedUserId == userId && currentProfileId != null) return

        viewModelScope.launch {
            _isLoading.value = true
            _homeError.value = null
            loadedUserId = userId

            val profileResult = profileRepository.getProfileByUserId(userId)

            if (profileResult.isSuccess) {
                currentProfileId = profileResult.getOrNull()?.id
            } else {
                _homeError.value = profileResult.exceptionOrNull()?.message
                    ?: "No se pudo obtener el perfil del usuario."
            }

            repository.getPopularRecipes()
                .onSuccess { _popularRecipes.value = it }
                .onFailure { _homeError.value = it.message }

            currentProfileId?.let { profileId ->
                repository.getSavedRecipes(profileId)
                    .onSuccess { _savedRecipes.value = it }
                    .onFailure { _homeError.value = it.message }
            }

            repository.getKitchenCategories()
                .onSuccess { _kitchenCategories.value = it }
                .onFailure { _homeError.value = it.message }

            _isLoading.value = false
        }
    }

    fun requestAiFeed(
        categoryId: Long,
        preference: String?,
        ingredients: List<String>?
    ) {
        viewModelScope.launch {
            _isFeedLoading.value = true
            _feedMessage.value = null

            repository.getAiFeed(categoryId, preference, ingredients)
                .onSuccess { feedResult ->
                    _aiTip.value = feedResult.aiTip
                    _recommendedRecipes.value = feedResult.recommendedRecipes
                    _feedMessage.value = if (feedResult.recommendedRecipes.isEmpty()) {
                        "No encontramos recetas sugeridas para esa combinación. Prueba con otra categoría o ingredientes."
                    } else {
                        null
                    }
                }
                .onFailure { throwable ->
                    _recommendedRecipes.value = emptyList()
                    _feedMessage.value = null
                    _homeError.value = throwable.message ?: "No se pudo generar el feed IA"
                }

            _isFeedLoading.value = false
        }
    }

    fun toggleSaveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val profileId = currentProfileId

            if (profileId == null) {
                _homeError.value = "No se pudo guardar: perfil no cargado."
                return@launch
            }

            val isSaved = _savedRecipes.value.any { it.id == recipe.id }

            val result = if (isSaved) {
                repository.removeSavedRecipe(profileId, recipe.id)
            } else {
                repository.saveRecipe(profileId, recipe.id)
            }

            if (result.isSuccess) {
                repository.getSavedRecipes(profileId)
                    .onSuccess { _savedRecipes.value = it }
                    .onFailure { _homeError.value = it.message }

                repository.getPopularRecipes()
                    .onSuccess { _popularRecipes.value = it }
            } else {
                _homeError.value = result.exceptionOrNull()?.message
            }
        }
    }
}
