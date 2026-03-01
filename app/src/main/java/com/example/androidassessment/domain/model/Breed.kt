package com.example.androidassessment.domain.model

data class Breed(
    val id: String = "",
    val name: String = "",
    val subBreeds: List<String> = emptyList()
)
