package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.Faq
import com.dishcovery.app.domain.repository.FaqRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: FaqRepository
) : ViewModel() {

    private val _faq       = MutableStateFlow<Faq?>(null)
    val faq: StateFlow<Faq?> = _faq.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error     = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado local del editor, separado del origen para poder cancelar
    private val _editContent = MutableStateFlow("")
    val editContent: StateFlow<String> = _editContent.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    init { loadFaq() }

    fun loadFaq() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getFaq()
                .onSuccess { faq ->
                    _faq.value       = faq
                    _editContent.value = faq.content
                    _error.value     = null
                }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun onContentChange(newText: String) {
        _editContent.value = newText
    }

    fun saveChanges() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateFaq(_editContent.value)
                .onSuccess { updated ->
                    _faq.value        = updated
                    _editContent.value = updated.content
                    _saveSuccess.value = true
                    _error.value      = null
                }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun resetSaveSuccess() { _saveSuccess.value = false }
}