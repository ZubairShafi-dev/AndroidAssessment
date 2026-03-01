package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.model.User
import com.example.androidassessment.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: Int): User? = repository.getUserById(id)
}
