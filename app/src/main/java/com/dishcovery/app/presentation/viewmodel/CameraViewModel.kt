package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.data.mapper.ImageMapper
import com.dishcovery.app.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val mapper: ImageMapper
) : ViewModel() {

    sealed class CameraUiState {
        object Idle : CameraUiState()
        object Loading : CameraUiState()
        data class Success(
            val tags: List<String>,
            val naturalText: String
        ) : CameraUiState()
        data class Error(val message: String) : CameraUiState()
    }

    private val _uiState = MutableStateFlow<CameraUiState>(CameraUiState.Idle)
    val uiState: StateFlow<CameraUiState> = _uiState

    fun analyzeImage(imageFile: File) {
        viewModelScope.launch {
            _uiState.value = CameraUiState.Loading
            repository.analyzeImage(imageFile)
                .onSuccess { result ->
                    _uiState.value = CameraUiState.Success(
                        tags = result.tags,
                        naturalText = mapper.toNaturalText(result.tags)
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = CameraUiState.Error(
                        throwable.message ?: "Error al analizar la imagen"
                    )
                }
        }
    }

    fun reset() {
        _uiState.value = CameraUiState.Idle
    }
}
