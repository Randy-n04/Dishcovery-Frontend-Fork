package com.dishcovery.app.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.R
import com.dishcovery.app.data.mapper.ImageMapper
import com.dishcovery.app.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val mapper: ImageMapper,
    @ApplicationContext private val context: Context
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
                        naturalText = toNaturalText(result.tags)
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = CameraUiState.Error(
                        throwable.message ?: "Error al analizar la imagen"
                    )
                }
        }
    }

    private fun toNaturalText(tags: List<String>): String = when {
        tags.isEmpty() -> context.getString(R.string.camera_no_ingredients)
        tags.size == 1 -> context.getString(R.string.camera_one_ingredient, tags[0])
        else -> {
            val allButLast = tags.dropLast(1).joinToString(", ")
            val last = tags.last()
            context.getString(R.string.camera_multiple_ingredients, allButLast, last)
        }
    }

    fun reset() {
        _uiState.value = CameraUiState.Idle
    }
}
