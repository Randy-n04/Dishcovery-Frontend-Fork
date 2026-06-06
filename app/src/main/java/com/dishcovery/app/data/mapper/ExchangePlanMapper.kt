package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.ExchangePlanInputDto
import com.dishcovery.app.data.datasource.model.ExchangePlanResultDto
import com.dishcovery.app.data.datasource.model.ExchangeSessionInputDto
import com.dishcovery.app.data.datasource.model.ExchangeSessionResultDto
import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.model.ExchangeSession
import com.dishcovery.app.domain.model.PlanStatus
import javax.inject.Inject

class ExchangePlanMapper @Inject constructor() {

    fun toDomain(dto: ExchangePlanResultDto): ExchangePlan {
        return ExchangePlan(
            id = dto.id,
            partnerId = dto.partnerId,
            partnerName = dto.partnerName,
            partnerImageUrl = dto.partnerImageUrl,
            sessions = dto.sessions.map { toDomain(it) },
            aiSuggestion = dto.aiSuggestion,
            progress = dto.progress,
            totalSessions = dto.totalSessions,
            status = dto.status.toPlanStatus(),
            userNote = dto.userNote
        )
    }

    fun toDomain(dto: ExchangeSessionResultDto): ExchangeSession {
        return ExchangeSession(
            id = dto.id,
            title = dto.title,
            instructorName = dto.instructorName,
            date = dto.date,
            time = dto.time,
            isCompleted = dto.isCompleted
        )
    }

    fun toInputDto(plan: ExchangePlan): ExchangePlanInputDto {
        return ExchangePlanInputDto(
            partnerId = plan.partnerId,
            partnerName = plan.partnerName,
            partnerImageUrl = plan.partnerImageUrl,
            sessions = plan.sessions.map { toInputDto(it) },
            aiSuggestion = plan.aiSuggestion,
            progress = plan.progress,
            totalSessions = plan.sessions.size,
            status = plan.status.name,
            userNote = plan.userNote
        )
    }

    fun toInputDto(session: ExchangeSession): ExchangeSessionInputDto {
        return ExchangeSessionInputDto(
            title = session.title,
            instructorName = session.instructorName,
            date = session.date,
            time = session.time,
            isCompleted = session.isCompleted
        )
    }

    private fun String.toPlanStatus(): PlanStatus {
        return runCatching {
            PlanStatus.valueOf(this)
        }.getOrDefault(PlanStatus.PENDING)
    }
}