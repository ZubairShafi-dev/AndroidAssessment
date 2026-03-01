package com.example.androidassessment.data.remote.posts

import com.example.androidassessment.data.model.PostResponse
import retrofit2.http.GET

interface PostsApiService {

    @GET("posts")
    suspend fun getPosts(): List<PostResponse>
}
