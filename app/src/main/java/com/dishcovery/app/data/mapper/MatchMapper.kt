package com.dishcovery.app.data.mapper

import com.dishcovery.app.data.datasource.model.MatchResultDto
import com.dishcovery.app.domain.model.Match
import javax.inject.Inject

class MatchMapper @Inject constructor() {
    fun toDomain(dto: MatchResultDto): Match {
        return Match(
            profileId = dto.profileId,
            publicName = dto.publicName,
            photoUrl = dto.photoUrl,
            location = dto.location,
            offeredSkills = dto.offeredSkills,
            skillsToLearn = dto.skillsToLearn,
            rank = dto.rank,
            reason = dto.reason
        )
    }

    fun toDomainList(dtos: List<MatchResultDto>): List<Match> {
        return dtos.map { toDomain(it) }
    }
}
