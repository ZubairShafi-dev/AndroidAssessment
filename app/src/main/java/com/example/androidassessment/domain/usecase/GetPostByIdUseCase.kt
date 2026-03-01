package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.model.Post
import com.example.androidassessment.domain.repository.PostRepository
import javax.inject.Inject

class GetPostByIdUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Int): Post? = repository.getPostById(id)
}
