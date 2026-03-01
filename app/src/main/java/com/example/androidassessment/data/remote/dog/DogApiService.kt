package com.example.androidassessment.data.remote.dog

import com.example.androidassessment.data.model.DogBreedsResponse
import retrofit2.http.GET

/**
 * Retrofit service for the Dog CEO API.
 * Base URL: https://dog.ceo/api/
 */
interface DogApiService {

    /**
     * Fetches all breeds with their sub-breeds.
     * GET https://dog.ceo/api/breeds/list/all
     */
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): DogBreedsResponse
}
