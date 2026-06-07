package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.StatPointDto
import com.dishcovery.app.domain.model.StatPoint
import javax.inject.Inject

class StatsMapper @Inject constructor() {
    fun toDomain(dto: StatPointDto): StatPoint = StatPoint(
        date  = dto.date,
        count = dto.count
    )
}