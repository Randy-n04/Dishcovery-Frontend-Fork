package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.FaqDto
import com.dishcovery.app.domain.model.Faq
import jakarta.inject.Inject

class FaqMapper @Inject constructor() {
    fun toDomain(dto: FaqDto): Faq = Faq(
        id      = dto.id,
        content = dto.content
    )
}