package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.FaqDto
import com.dishcovery.app.data.datasource.model.FaqUpdateRequest
import com.dishcovery.app.data.datasource.remote.FaqApiService
import jakarta.inject.Inject

class FaqDataSourceImpl @Inject constructor(
    private val apiService: FaqApiService
) : FaqDataSource {

    override suspend fun getFaq(): FaqDto? {
        return try {
            apiService.getFaq()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateFaq(content: String): FaqDto? {
        return try {
            apiService.updateFaq(FaqUpdateRequest(content))
        } catch (e: Exception) {
            null
        }
    }
}