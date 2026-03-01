package com.example.androidassessment.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO for a single post from GET /posts (JSONPlaceholder).
 */
data class PostResponse(
    @SerializedName("id")     val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("title")  val title: String,
    @SerializedName("body")   val body: String
)
