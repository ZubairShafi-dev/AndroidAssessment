package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.model.Breed
import com.example.androidassessment.domain.repository.DogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns a reactive [Flow] of cached [Breed] items from Room.
 * Pair with [RefreshBreedsUseCase] to trigger a network/mock fetch.
 */
class GetBreedsUseCase @Inject constructor(
    private val repository: DogRepository
) {
    operator fun invoke(): Flow<List<Breed>> = repository.getBreedsStream()
}
