package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.model.RandomUser
import com.example.androidassessment.domain.repository.RandomUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRandomUsersUseCase @Inject constructor(
    private val repository: RandomUserRepository
) {
    operator fun invoke(): Flow<List<RandomUser>> = repository.getRandomUsersStream()
}
