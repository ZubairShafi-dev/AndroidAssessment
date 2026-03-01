package com.example.androidassessment.domain.repository

import com.example.androidassessment.domain.model.Breed
import kotlinx.coroutines.flow.Flow

interface DogRepository {
    fun getBreedsStream(): Flow<List<Breed>>
    suspend fun refreshBreeds()
    suspend fun getBreedByName(name: String): Breed?
}
