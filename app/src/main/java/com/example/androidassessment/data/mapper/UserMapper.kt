package com.example.androidassessment.data.mapper

import com.example.androidassessment.data.local.entity.UserEntity
import com.example.androidassessment.data.model.UserResponse
import com.example.androidassessment.domain.model.User

// ── Remote DTO → Entity ───────────────────────────────────────────────────────

fun UserResponse.toEntity(): UserEntity =
    UserEntity(
        id = id,
        name = name,
        username = username,
        email = email,
        phone = phone,
        website = website,
        addressStreet = address?.street ?: "",
        addressCity = address?.city ?: "",
        addressZipcode = address?.zipcode ?: "",
        companyName = company?.name ?: ""
    )

// ── Entity → Domain ───────────────────────────────────────────────────────────

fun UserEntity.toDomain(): User =
    User(
        id = id,
        name = name,
        username = username,
        email = email,
        phone = phone,
        website = website,
        addressStreet = addressStreet,
        addressCity = addressCity,
        addressZipcode = addressZipcode,
        companyName = companyName
    )

fun List<UserEntity>.toDomainList(): List<User> = map { it.toDomain() }

/** Standalone mapper for single UserEntity (avoids import conflict with BreedMapper.toDomain). */
fun userEntityToDomain(entity: UserEntity): User = entity.toDomain()
