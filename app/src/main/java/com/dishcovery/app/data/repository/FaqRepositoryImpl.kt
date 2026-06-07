package com.dishcovery.app.data.repository

import com.dishcovery.app.data.datasource.FaqDataSource
import com.dishcovery.app.data.mapper.FaqMapper
import com.dishcovery.app.domain.error.DomainError
import com.dishcovery.app.domain.model.Faq
import com.dishcovery.app.domain.repository.FaqRepository
import jakarta.inject.Inject

class FaqRepositoryImpl @Inject constructor(
    private val dataSource: FaqDataSource,
    private val mapper: FaqMapper
) : FaqRepository {

    override suspend fun getFaq(): Result<Faq> {
        return try {
            val dto = dataSource.getFaq()
            if (dto != null) Result.success(mapper.toDomain(dto))
            else Result.failure(DomainError.UnknownError())
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }

    override suspend fun updateFaq(content: String): Result<Faq> {
        return try {
            val dto = dataSource.updateFaq(content)
            if (dto != null) Result.success(mapper.toDomain(dto))
            else Result.failure(DomainError.UnknownError())
        } catch (e: Exception) {
            Result.failure(DomainError.UnknownError(cause = e))
        }
    }
}