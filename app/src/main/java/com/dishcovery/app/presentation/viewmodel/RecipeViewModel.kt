package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.error.DomainError
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
    private val profileRepository: ProfileRepository,
    private val globalErrorViewModel: GlobalErrorViewModel
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isFeedLoading = MutableStateFlow(false)
    val isFeedLoading: StateFlow<Boolean> = _isFeedLoading.asStateFlow()

    val homeError: StateFlow<DomainError?> = globalErrorViewModel.error

    private var currentProfileId: Long? = null
    private var loadedUserId: Long? = null

    fun loadHomeData(userId: Long?) {
        if (userId == null) {
            return
        }

        if (loadedUserId == userId && currentProfileId != null) return

        viewModelScope.launch {
            _isLoading.value = true
            loadedUserId = userId

            val profileResult = profileRepository.getProfileByUserId(userId)

            if (profileResult.isSuccess) {
                currentProfileId = profileResult.getOrNull()?.id
            } else {
                profileResult.exceptionOrNull()?.let { globalErrorViewModel.showError(it) }
            }

            repository.getPopularRecipes()
                .onSuccess { _popularRecipes.value = it }
                .onFailure { globalErrorViewModel.showError(it) }

            currentProfileId?.let { profileId ->
                repository.getSavedRecipes(profileId)
                    .onSuccess { _savedRecipes.value = it }
                    .onFailure { globalErrorViewModel.showError(it) }
            }

            repository.getKitchenCategories()
                .onSuccess { _kitchenCategories.value = it }
                .onFailure { globalErrorViewModel.showError(it) }

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
                    if (feedResult.recommendedRecipes.isEmpty()) {
                        _feedMessage.value = "No encontramos recetas sugeridas para esa combinación."
                    }
                }
                .onFailure { throwable ->
                    _recommendedRecipes.value = emptyList()
                    globalErrorViewModel.showError(throwable)
                }

            _isFeedLoading.value = false
        }
    }

    fun toggleSaveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val profileId = currentProfileId ?: return@launch

            val isSaved = _savedRecipes.value.any { it.id == recipe.id }

            val result = if (isSaved) {
                repository.removeSavedRecipe(profileId, recipe.id)
            } else {
                repository.saveRecipe(profileId, recipe.id)
            }

            if (result.isSuccess) {
                repository.getSavedRecipes(profileId)
                    .onSuccess { _savedRecipes.value = it }
                    .onFailure { globalErrorViewModel.showError(it) }

                repository.getPopularRecipes()
                    .onSuccess { _popularRecipes.value = it }
            } else {
                result.exceptionOrNull()?.let { globalErrorViewModel.showError(it) }
            }
        }
    }
}
