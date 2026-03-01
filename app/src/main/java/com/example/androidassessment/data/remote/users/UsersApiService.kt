package com.example.androidassessment.data.remote.users

import com.example.androidassessment.data.model.UserResponse
import retrofit2.http.GET

/**
 * Retrofit service for the JSONPlaceholder Users API.
 * Base URL: https://jsonplaceholder.typicode.com/
 */
interface UsersApiService {

    /**
     * Fetches all users.
     * GET https://jsonplaceholder.typicode.com/users
     */
    @GET("users")
    suspend fun getUsers(): List<UserResponse>
}
