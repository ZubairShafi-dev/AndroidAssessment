package com.example.androidassessment.data.mapper

import com.example.androidassessment.data.local.entity.PostEntity
import com.example.androidassessment.data.model.PostResponse
import com.example.androidassessment.domain.model.Post

// ── DTO → Entity ──────────────────────────────────────────────────────────────

fun PostResponse.toEntity() = PostEntity(
    id     = id,
    userId = userId,
    title  = title,
    body   = body
)

fun List<PostResponse>.toEntityList() = map { it.toEntity() }

// ── Entity → Domain ───────────────────────────────────────────────────────────

fun PostEntity.toDomain() = Post(
    id     = id,
    userId = userId,
    title  = title,
    body   = body
)

fun List<PostEntity>.toDomainList() = map { it.toDomain() }
