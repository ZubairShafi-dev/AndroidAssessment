package com.example.androidassessment.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a dog breed cached locally.
 *
 * [subBreeds] is a [List]<[String]> stored via [com.example.androidassessment.data.local.converter.StringListConverter].
 * Do NOT expose this entity outside the data layer — map to domain [com.example.androidassessment.domain.model.Breed].
 */
@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey
    @ColumnInfo(name = "breed_name")
    val breedName: String,

    @ColumnInfo(name = "sub_breeds")
    val subBreeds: List<String> = emptyList()
)
