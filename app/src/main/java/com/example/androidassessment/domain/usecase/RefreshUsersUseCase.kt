package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.repository.UserRepository
import javax.inject.Inject

class RefreshUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.refreshUsers()
}
