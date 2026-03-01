package com.example.androidassessment.data.remote

import com.example.androidassessment.data.model.UserResponse

interface UserRemoteDataSource {
    suspend fun getUsers(): List<UserResponse>
}
