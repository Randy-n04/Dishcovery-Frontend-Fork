package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.FaqDto

interface FaqDataSource {
    suspend fun getFaq(): FaqDto?
    suspend fun updateFaq(content: String): FaqDto?
}