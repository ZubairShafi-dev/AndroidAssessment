package com.example.androidassessment.data.repository

import com.example.androidassessment.BuildConfig
import com.example.androidassessment.data.local.dao.RandomUserDao
import com.example.androidassessment.data.mapper.toDomainList
import com.example.androidassessment.data.mapper.toEntityList
import com.example.androidassessment.data.mock.MockRandomUserDataSource
import com.example.androidassessment.data.remote.randomuser.RandomUserApiService
import com.example.androidassessment.domain.model.RandomUser
import com.example.androidassessment.domain.repository.RandomUserRepository
import com.example.androidassessment.util.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RandomUserRepositoryImpl @Inject constructor(
    private val randomUserDao: RandomUserDao,
    private val randomUserApiService: RandomUserApiService,
    private val mockDataSource: MockRandomUserDataSource
) : RandomUserRepository {

    private val tag = "RandomUserRepositoryImpl"

    override fun getRandomUsersStream(): Flow<List<RandomUser>> =
        randomUserDao.observeAllRandomUsers().map { it.toDomainList() }

    override suspend fun refreshRandomUsers() {
        AppLogger.d(tag, "refreshRandomUsers() — USE_MOCK_DATA=${BuildConfig.USE_MOCK_DATA}")
        val entities = if (BuildConfig.USE_MOCK_DATA) {
            AppLogger.i(tag, "Loading mock random users")
            mockDataSource.getRandomUsers().results.toEntityList()
        } else {
            AppLogger.i(tag, "Fetching random users from remote")
            try {
                val response = randomUserApiService.getUsers(10)
                AppLogger.d(tag, "Fetched ${response.results.size} random users from API")
                response.results.toEntityList()
            } catch (e: Exception) {
                AppLogger.e(tag, "Failed to fetch random users", e)
                throw e
            }
        }
        randomUserDao.clearAll()
        randomUserDao.insertAll(entities)
        AppLogger.d(tag, "Saved ${entities.size} random users to Room")
    }
}
