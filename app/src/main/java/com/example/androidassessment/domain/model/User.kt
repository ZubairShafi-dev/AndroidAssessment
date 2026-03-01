package com.example.androidassessment.domain.model

/**
 * Domain model for a user.
 * Pure Kotlin — no Android or framework dependencies.
 */
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val addressStreet: String = "",
    val addressCity: String = "",
    val addressZipcode: String = "",
    val companyName: String = ""
)
