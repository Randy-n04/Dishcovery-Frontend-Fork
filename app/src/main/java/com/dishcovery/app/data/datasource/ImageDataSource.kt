package com.dishcovery.app.data.datasource

import java.io.File

interface ImageDataSource {
    suspend fun analyzeImage(imageFile: File): List<String>
}
