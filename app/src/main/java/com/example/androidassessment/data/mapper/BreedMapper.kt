package com.example.androidassessment.data.mapper

import com.example.androidassessment.data.local.entity.BreedEntity
import com.example.androidassessment.data.model.DogBreedsResponse
import com.example.androidassessment.domain.model.Breed

// ── Remote DTO → Entity ───────────────────────────────────────────────────────

/**
 * Converts a [DogBreedsResponse] (whose message is a breed→sub-breeds map)
 * into a flat list of [BreedEntity] ready for Room insertion.
 */
fun DogBreedsResponse.toEntityList(): List<BreedEntity> =
    message.entries.map { (breedName, subBreeds) ->
        BreedEntity(
            breedName = breedName,
            subBreeds = subBreeds
        )
    }

// ── Entity → Domain ───────────────────────────────────────────────────────────

fun BreedEntity.toDomain(): Breed =
    Breed(
        id = breedName,
        name = breedName.replaceFirstChar { it.uppercaseChar() },
        subBreeds = subBreeds
    )

fun List<BreedEntity>.toDomainList(): List<Breed> = map { it.toDomain() }

// ── Mock map → Entity ─────────────────────────────────────────────────────────

fun Map<String, List<String>>.toEntityList(): List<BreedEntity> =
    entries.map { (name, subs) -> BreedEntity(breedName = name, subBreeds = subs) }
