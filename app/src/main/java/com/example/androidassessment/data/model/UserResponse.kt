package com.example.androidassessment.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO for a single user returned by GET /users (JSONPlaceholder).
 *
 * Example item:
 * {
 *   "id": 1,
 *   "name": "Leanne Graham",
 *   "username": "Bret",
 *   "email": "Sincere@april.biz",
 *   "address": { "street": "Kulas Light", "suite": "Apt. 556",
 *                "city": "Gwenborough", "zipcode": "92998-3874",
 *                "geo": { "lat": "-37.3159", "lng": "81.1496" } },
 *   "phone": "1-770-736-8031 x56442",
 *   "website": "hildegard.org",
 *   "company": { "name": "Romaguera-Crona", "catchPhrase": "...", "bs": "..." }
 * }
 */
data class UserResponse(
    @SerializedName("id")       val id: Int,
    @SerializedName("name")     val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email")    val email: String,
    @SerializedName("phone")    val phone: String,
    @SerializedName("website")  val website: String,
    @SerializedName("address")  val address: AddressDto? = null,
    @SerializedName("company")  val company: CompanyDto? = null
)

data class AddressDto(
    @SerializedName("street")  val street: String,
    @SerializedName("suite")   val suite: String,
    @SerializedName("city")    val city: String,
    @SerializedName("zipcode") val zipcode: String,
    @SerializedName("geo")     val geo: GeoDto
)

data class GeoDto(
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String
)

data class CompanyDto(
    @SerializedName("name")         val name: String,
    @SerializedName("catchPhrase")  val catchPhrase: String,
    @SerializedName("bs")           val bs: String
)
