package com.example.androidassessment.data.repository

import com.example.androidassessment.BuildConfig
import com.example.androidassessment.data.local.dao.PostDao
import com.example.androidassessment.data.mapper.toDomain
import com.example.androidassessment.data.mapper.toDomainList
import com.example.androidassessment.data.mapper.toEntityList
import com.example.androidassessment.data.mock.MockPostDataSource
import com.example.androidassessment.data.remote.posts.PostsApiService
import com.example.androidassessment.domain.model.Post
import com.example.androidassessment.domain.repository.PostRepository
import com.example.androidassessment.util.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val postsApiService: PostsApiService,
    private val mockDataSource: MockPostDataSource
) : PostRepository {

    private val tag = "PostRepositoryImpl"

    override fun getPostsStream(): Flow<List<Post>> =
        postDao.observeAllPosts().map { it.toDomainList() }

    override suspend fun refreshPosts() {
        AppLogger.d(tag, "refreshPosts() — USE_MOCK_DATA=${BuildConfig.USE_MOCK_DATA}")
        val entities = if (BuildConfig.USE_MOCK_DATA) {
            AppLogger.i(tag, "Loading mock posts")
            mockDataSource.getPosts().toEntityList()
        } else {
            AppLogger.i(tag, "Fetching posts from remote")
            try {
                val dtos = postsApiService.getPosts()
                AppLogger.d(tag, "Fetched ${dtos.size} posts from API")
                dtos.toEntityList()
            } catch (e: Exception) {
                AppLogger.e(tag, "Failed to fetch posts", e)
                throw e
            }
        }
        postDao.clearAll()
        postDao.insertAll(entities)
        AppLogger.d(tag, "Saved ${entities.size} posts to Room")
    }

    override suspend fun getPostById(id: Int): Post? =
        postDao.getPostById(id)?.toDomain()
}
