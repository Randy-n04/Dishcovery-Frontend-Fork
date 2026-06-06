package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.repository.KitchenCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminCategoryViewModel @Inject constructor(
    private val repository: KitchenCategoryRepository
) : ViewModel() {

    private val _categories    = MutableStateFlow<List<KitchenCategory>>(emptyList())
    val categories: StateFlow<List<KitchenCategory>> = _categories.asStateFlow()

    private val _isLoading     = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error         = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success       = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    init { loadCategories() }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAll()
                .onSuccess { _categories.value = it; _error.value = null }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun setTrending(id: Long, days: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.setTrending(id, days)
                .onSuccess { _success.value = true; _error.value = null }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun resetSuccess() { _success.value = false }
}