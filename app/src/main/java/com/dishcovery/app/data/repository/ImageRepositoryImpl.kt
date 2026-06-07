package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.ImageDataSource
import com.dishcovery.app.data.mapper.ImageMapper
import com.dishcovery.app.domain.model.ImageAnalysisResult
import com.dishcovery.app.domain.repository.ImageRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val dataSource: ImageDataSource,
    private val mapper: ImageMapper
) : ImageRepository {

    override suspend fun analyzeImage(imageFile: File): Result<ImageAnalysisResult> = try {
        val tags = dataSource.analyzeImage(imageFile)
        Result.success(mapper.toDomain(tags))
    } catch (e: IOException) {
        Result.failure(Exception("Error de conexión al analizar la imagen", e))
    } catch (e: Exception) {
        Result.failure(Exception(e.message ?: "Error inesperado al analizar la imagen", e))
    }
}
