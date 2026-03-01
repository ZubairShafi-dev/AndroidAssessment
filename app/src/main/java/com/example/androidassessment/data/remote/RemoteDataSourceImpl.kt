package com.example.androidassessment.data.remote

import com.example.androidassessment.data.model.BreedDto
import com.example.androidassessment.data.model.DogBreedsResponse
import com.example.androidassessment.data.remote.dog.DogApiService
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val dogApiService: DogApiService
) : RemoteDataSource {

    override suspend fun getBreeds(): List<BreedDto> {
        val response: DogBreedsResponse = dogApiService.getAllBreeds()
        return response.message.entries.map { (name, subBreeds) ->
            BreedDto(name = name, subBreeds = subBreeds)
        }
    }

    override suspend fun getBreedById(id: String): BreedDto? =
        getBreeds().firstOrNull { it.name == id }
}
