package com.example.androidassessment.data.model

import com.google.gson.annotations.SerializedName

/**
 * Top-level response from GET /breeds/list/all
 *
 * Example:
 * {
 *   "message": { "affenpinscher": [], "african": [], "bulldog": ["boston","english","french"] },
 *   "status": "success"
 * }
 */
data class DogBreedsResponse(
    /** Map of breed name → list of sub-breed names (empty if none). */
    @SerializedName("message") val message: Map<String, List<String>>,
    @SerializedName("status")  val status: String
)
