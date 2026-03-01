package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.repository.DogRepository
import javax.inject.Inject

/**
 * Triggers a refresh of the breed list from the configured data source
 * (remote or mock). Throws on failure; the ViewModel is responsible for
 * catching and mapping to a UI error state.
 */
class RefreshBreedsUseCase @Inject constructor(
    private val repository: DogRepository
) {
    suspend operator fun invoke() = repository.refreshBreeds()
}
