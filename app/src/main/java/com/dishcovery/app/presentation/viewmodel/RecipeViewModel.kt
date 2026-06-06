package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
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

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            _homeError.value = null
            
            val popularResult = repository.getPopularRecipes()
            if (popularResult.isSuccess) {
                _popularRecipes.value = popularResult.getOrDefault(emptyList())
            } else {
                _homeError.value = popularResult.exceptionOrNull()?.message
                    ?: "No se pudieron cargar las recetas populares"
            }

            val savedResult = repository.getSavedRecipes()
            if (savedResult.isSuccess) {
                _savedRecipes.value = savedResult.getOrDefault(emptyList())
            } else if (_homeError.value == null) {
                _homeError.value = savedResult.exceptionOrNull()?.message
                    ?: "No se pudieron cargar las recetas guardadas"
            } else {
                _homeError.value = buildString {
                    append(_homeError.value)
                    append(" • ")
                    append(savedResult.exceptionOrNull()?.message ?: "No se pudieron cargar las recetas guardadas")
                }
            }

            val categoriesResult = repository.getKitchenCategories()
            if (categoriesResult.isSuccess) {
                _kitchenCategories.value = categoriesResult.getOrDefault(emptyList())
            } else if (_homeError.value == null) {
                _homeError.value = categoriesResult.exceptionOrNull()?.message
                    ?: "No se pudieron cargar las categorías"
            } else {
                _homeError.value = buildString {
                    append(_homeError.value)
                    append(" • ")
                    append(categoriesResult.exceptionOrNull()?.message ?: "No se pudieron cargar las categorías")
                }
            }

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
            val isSaved = _savedRecipes.value.any { it.id == recipe.id }
            if (isSaved) {
                repository.removeSavedRecipe(recipe.id)
            } else {
                repository.saveRecipe(recipe)
            }
            // Refresh saved recipes
            val savedResult = repository.getSavedRecipes()
            if (savedResult.isSuccess) {
                _savedRecipes.value = savedResult.getOrDefault(emptyList())
            } else {
                _homeError.value = savedResult.exceptionOrNull()?.message
            }
        }
    }
}
