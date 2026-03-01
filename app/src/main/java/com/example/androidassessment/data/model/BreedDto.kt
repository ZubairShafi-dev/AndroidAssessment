package com.example.androidassessment.data.model

import com.google.gson.annotations.SerializedName

data class BreedDto(
    @SerializedName("name")       val name: String = "",
    @SerializedName("sub_breeds") val subBreeds: List<String> = emptyList()
)
