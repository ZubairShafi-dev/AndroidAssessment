package com.example.androidassessment.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO for GET https://randomuser.me/api/?results=10
 */
data class RandomUserResponse(
    @SerializedName("results") val results: List<RandomUserResult>
)

data class RandomUserResult(
    @SerializedName("name")     val name: RandomUserName,
    @SerializedName("email")     val email: String,
    @SerializedName("phone")    val phone: String,
    @SerializedName("location") val location: RandomUserLocation?
)

data class RandomUserName(
    @SerializedName("first") val first: String,
    @SerializedName("last")  val last: String
)

data class RandomUserLocation(
    @SerializedName("street") val street: RandomUserStreet?,
    @SerializedName("city")   val city: String?,
    @SerializedName("state") val state: String?
)

data class RandomUserStreet(
    @SerializedName("name") val name: String?
)
