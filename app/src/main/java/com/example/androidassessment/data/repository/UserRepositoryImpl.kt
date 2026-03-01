package com.example.androidassessment.data.repository

import com.example.androidassessment.BuildConfig
import com.example.androidassessment.data.local.dao.UserDao
import com.example.androidassessment.data.mapper.toDomainList
import com.example.androidassessment.data.mapper.toEntity
import com.example.androidassessment.data.mapper.userEntityToDomain
import com.example.androidassessment.data.mock.MockUserDataSource
import com.example.androidassessment.data.remote.UserRemoteDataSource
import com.example.androidassessment.domain.model.User
import com.example.androidassessment.domain.repository.UserRepository
import com.example.androidassessment.util.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val remoteDataSource: UserRemoteDataSource,
    private val mockDataSource: MockUserDataSource
) : UserRepository {

    private val tag = "UserRepositoryImpl"

    override fun getUsersStream(): Flow<List<User>> =
        userDao.observeAllUsers().map { entities -> entities.toDomainList() }

    override suspend fun refreshUsers() {
        AppLogger.d(tag, "refreshUsers() — USE_MOCK_DATA=${BuildConfig.USE_MOCK_DATA}")

        val entities = if (BuildConfig.USE_MOCK_DATA) {
            AppLogger.i(tag, "Loading mock users")
            mockDataSource.getUserEntities()
        } else {
            AppLogger.i(tag, "Fetching users from remote")
            try {
                val responses = remoteDataSource.getUsers()
                AppLogger.d(tag, "Fetched ${responses.size} users from API")
                responses.map { it.toEntity() }
            } catch (e: Exception) {
                AppLogger.e(tag, "Failed to fetch users from remote", e)
                throw e
            }
        }

        userDao.clearAll()
        userDao.insertAll(entities)
        AppLogger.d(tag, "Saved ${entities.size} users to Room")
    }

    override suspend fun getUserById(id: Int): User? =
        userDao.getUserById(id)?.let { userEntityToDomain(it) }
}
