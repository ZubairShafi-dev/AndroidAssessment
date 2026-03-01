package com.example.androidassessment.domain.usecase

import com.example.androidassessment.domain.repository.PostRepository
import javax.inject.Inject

class RefreshPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke() = repository.refreshPosts()
}
