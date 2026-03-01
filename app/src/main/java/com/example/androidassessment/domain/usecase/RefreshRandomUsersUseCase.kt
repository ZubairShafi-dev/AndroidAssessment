package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.repository.RandomUserRepository
import javax.inject.Inject

class RefreshRandomUsersUseCase @Inject constructor(
    private val repository: RandomUserRepository
) {
    suspend operator fun invoke() = repository.refreshRandomUsers()
}
