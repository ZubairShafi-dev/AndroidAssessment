package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.model.Post
import com.example.androidassessment.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(): Flow<List<Post>> = repository.getPostsStream()
}
