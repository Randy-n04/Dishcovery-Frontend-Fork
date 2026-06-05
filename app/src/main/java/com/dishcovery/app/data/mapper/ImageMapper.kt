package com.dishcovery.app.data.mapper

import com.dishcovery.app.domain.model.ImageAnalysisResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageMapper @Inject constructor() {

    /** Convierte la lista de tags del backend al modelo de dominio. */
    fun toDomain(tags: List<String>): ImageAnalysisResult = ImageAnalysisResult(tags = tags)

    /**
     * Convierte la lista de tags a texto natural en español.
     * Los tags vienen en inglés desde el backend (ej: ["paella", "seafood"]),
     * por lo que el texto generado mezclará ambos idiomas salvo que se añada
     * una capa de traducción en el futuro.
     */
    fun toNaturalText(tags: List<String>): String = when {
        tags.isEmpty() -> "No se detectaron ingredientes en la imagen."
        tags.size == 1 -> "Se detectó: ${tags[0]}."
        else -> {
            val rest = tags.dropLast(1).joinToString(", ")
            val last = tags.last()
            "Se detectaron los siguientes ingredientes: $rest y $last."
        }
    }
}
