package com.dishcovery.app.domain.repository

import com.dishcovery.app.domain.model.Faq

interface FaqRepository {
    suspend fun getFaq(): Result<Faq>
    suspend fun updateFaq(content: String): Result<Faq>
}