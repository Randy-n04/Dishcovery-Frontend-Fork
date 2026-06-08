package com.dishcovery.app.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
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
import java.io.FileOutputStream
import javax.inject.Inject
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val mapper: ImageMapper,
    @ApplicationContext private val context: Context,
    private val globalErrorViewModel: GlobalErrorViewModel
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

            // Comprimir antes de enviar
            val compressedFile = withContext(Dispatchers.IO) {
                compressImage(imageFile)
            }

            repository.analyzeImage(compressedFile)
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
                    globalErrorViewModel.showError(throwable)
                }
        }
    }

    private fun compressImage(originalFile: File): File {
        return try {
            val originalBitmap = BitmapFactory.decodeFile(originalFile.absolutePath)
                ?: return originalFile // No se pudo decodificar, usar original

            // Corregir rotación EXIF (problema común en fotos de cámara real)
            val rotatedBitmap = fixExifRotation(originalFile, originalBitmap)

            // Calcular nueva dimensión manteniendo aspecto
            val maxDimension = 1024
            val scale = minOf(
                maxDimension.toFloat() / rotatedBitmap.width,
                maxDimension.toFloat() / rotatedBitmap.height,
                1.0f // No agrandar si ya es pequeña
            )

            val finalBitmap = if (scale < 1.0f) {
                rotatedBitmap.scale(
                    (rotatedBitmap.width * scale).toInt(),
                    (rotatedBitmap.height * scale).toInt()
                )
            } else {
                rotatedBitmap
            }

            // Guardar en archivo temporal separado
            val compressedFile = File(context.cacheDir, "compressed_photo.jpg")
            FileOutputStream(compressedFile).use { out ->
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }

            // Limpiar bitmaps de memoria
            if (rotatedBitmap !== originalBitmap) rotatedBitmap.recycle()
            if (finalBitmap !== rotatedBitmap) finalBitmap.recycle()
            originalBitmap.recycle()

            compressedFile
        } catch (e: Exception) {
            originalFile // Fallback al original si algo falla
        }
    }

    private fun fixExifRotation(file: File, bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(file.absolutePath)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        val degrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90  -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> return bitmap // Sin rotación, devolver tal cual
        }
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
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
