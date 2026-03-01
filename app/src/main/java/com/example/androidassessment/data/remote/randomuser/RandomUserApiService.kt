package com.example.androidassessment.data.remote.randomuser

import com.example.androidassessment.data.model.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApiService {

    @GET("api/")
    suspend fun getUsers(@Query("results") results: Int = 10): RandomUserResponse
}
