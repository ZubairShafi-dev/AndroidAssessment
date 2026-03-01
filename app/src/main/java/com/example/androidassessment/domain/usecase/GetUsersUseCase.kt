package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.model.User
import com.example.androidassessment.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> = repository.getUsersStream()
}
