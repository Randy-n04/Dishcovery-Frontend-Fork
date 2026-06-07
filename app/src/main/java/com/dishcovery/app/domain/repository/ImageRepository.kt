package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.ImageAnalysisResult
import java.io.File

interface ImageRepository {
    suspend fun analyzeImage(imageFile: File): Result<ImageAnalysisResult>
}
