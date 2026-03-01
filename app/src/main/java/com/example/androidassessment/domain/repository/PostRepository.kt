package com.example.androidassessment.domain.repository

import com.example.androidassessment.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostsStream(): Flow<List<Post>>
    suspend fun refreshPosts()
    suspend fun getPostById(id: Int): Post?
}
