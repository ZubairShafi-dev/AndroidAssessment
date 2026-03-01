package com.example.androidassessment.data.mapper

import com.example.androidassessment.data.local.entity.RandomUserEntity
import com.example.androidassessment.data.model.RandomUserResult
import com.example.androidassessment.domain.model.RandomUser

// ── DTO → Entity ──────────────────────────────────────────────────────────────

fun RandomUserResult.toEntity() = RandomUserEntity(
    fullName = "${name.first} ${name.last}",
    email    = email,
    phone    = phone,
    city     = location?.city.orEmpty(),
    state    = location?.state.orEmpty()
)

fun List<RandomUserResult>.toEntityList() = map { it.toEntity() }

// ── Entity → Domain ───────────────────────────────────────────────────────────

fun RandomUserEntity.toDomain() = RandomUser(
    id       = id,
    fullName = fullName,
    email    = email,
    phone    = phone,
    city     = city,
    state    = state
)

fun List<RandomUserEntity>.toDomainList() = map { it.toDomain() }
