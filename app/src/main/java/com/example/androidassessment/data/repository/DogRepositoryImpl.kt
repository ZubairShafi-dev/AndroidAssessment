package com.example.androidassessment.data.repository

import com.example.androidassessment.BuildConfig
import com.example.androidassessment.data.local.dao.BreedDao
import com.example.androidassessment.data.mapper.toDomainList
import com.example.androidassessment.data.mock.MockDogDataSource
import com.example.androidassessment.data.remote.RemoteDataSource
import com.example.androidassessment.data.local.entity.BreedEntity
import com.example.androidassessment.domain.model.Breed
import com.example.androidassessment.domain.repository.DogRepository
import com.example.androidassessment.util.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(
    private val breedDao: BreedDao,
    private val remoteDataSource: RemoteDataSource,
    private val mockDataSource: MockDogDataSource
) : DogRepository {

    private val tag = "DogRepositoryImpl"

    override fun getBreedsStream(): Flow<List<Breed>> =
        breedDao.observeAllBreeds().map { entities -> entities.toDomainList() }

    override suspend fun refreshBreeds() {
        AppLogger.d(tag, "refreshBreeds() — USE_MOCK_DATA=${BuildConfig.USE_MOCK_DATA}")
        val entities = if (BuildConfig.USE_MOCK_DATA) {
            AppLogger.i(tag, "Loading mock breeds")
            mockDataSource.getBreedEntities()
        } else {
            AppLogger.i(tag, "Fetching breeds from remote")
            try {
                val dtos = remoteDataSource.getBreeds()
                AppLogger.d(tag, "Fetched ${dtos.size} breeds from API")
                dtos.map { dto ->
                    BreedEntity(breedName = dto.name, subBreeds = dto.subBreeds)
                }
            } catch (e: Exception) {
                AppLogger.e(tag, "Failed to fetch breeds from remote", e)
                throw e
            }
        }
        breedDao.clearAll()
        breedDao.insertAll(entities)
        AppLogger.d(tag, "Saved ${entities.size} breeds to Room")
    }

    override suspend fun getBreedByName(name: String): Breed? =
        breedDao.getBreedByName(name)?.let { entity ->
            Breed(
                id = entity.breedName,
                name = entity.breedName.replaceFirstChar { it.uppercaseChar() },
                subBreeds = entity.subBreeds
            )
        }
}
