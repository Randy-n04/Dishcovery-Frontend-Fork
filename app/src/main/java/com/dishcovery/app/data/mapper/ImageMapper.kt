package com.dishcovery.app.data.mapper

import com.dishcovery.app.domain.model.ImageAnalysisResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageMapper @Inject constructor() {

    /** Convierte la lista de tags del backend al modelo de dominio. */
    fun toDomain(tags: List<String>): ImageAnalysisResult = ImageAnalysisResult(tags = tags)

}
