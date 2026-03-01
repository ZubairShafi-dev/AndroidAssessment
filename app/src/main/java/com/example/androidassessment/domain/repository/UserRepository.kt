package com.example.androidassessment.domain.repository

import com.example.androidassessment.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /**
     * Reactive stream of cached users from local database.
     * Call [refreshUsers] to populate/update.
     */
    fun getUsersStream(): Flow<List<User>>

    /**
     * Fetch users from the configured data source (remote or mock),
     * persist to Room, and return. Callers observe [getUsersStream] for UI updates.
     */
    suspend fun refreshUsers()

    suspend fun getUserById(id: Int): User?
}
