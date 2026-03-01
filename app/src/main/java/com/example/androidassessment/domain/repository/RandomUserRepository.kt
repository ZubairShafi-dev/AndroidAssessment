package com.example.androidassessment.domain.repository

import com.example.androidassessment.domain.model.RandomUser
import kotlinx.coroutines.flow.Flow

interface RandomUserRepository {
    fun getRandomUsersStream(): Flow<List<RandomUser>>
    suspend fun refreshRandomUsers()
}
