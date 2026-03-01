package com.example.androidassessment.data.remote

import com.example.androidassessment.data.model.UserResponse
import com.example.androidassessment.data.remote.users.UsersApiService
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val usersApiService: UsersApiService
) : UserRemoteDataSource {

    override suspend fun getUsers(): List<UserResponse> =
        usersApiService.getUsers()
}
