package com.example.androidassessment.data.remote

import com.example.androidassessment.data.model.BreedDto

interface RemoteDataSource {

    suspend fun getBreeds(): List<BreedDto>

    suspend fun getBreedById(id: String): BreedDto?
}
