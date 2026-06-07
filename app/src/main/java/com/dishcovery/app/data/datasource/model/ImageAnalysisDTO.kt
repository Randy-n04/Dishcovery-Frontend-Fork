package com.dishcovery.app.data.datasource.model

import com.google.gson.annotations.SerializedName

data class ImageAnalysisDTO(
    @SerializedName("tags") val tags: List<String>
)