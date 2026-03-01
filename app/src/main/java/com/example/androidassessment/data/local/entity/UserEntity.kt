package com.example.androidassessment.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a user cached locally.
 *
 * Only the fields relevant to display are persisted; for full detail fetch from remote.
 * Do NOT expose this entity outside the data layer — map to domain model as needed.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "website")
    val website: String,

    /** Flattened address fields to avoid a joined table for this simple model. */
    @ColumnInfo(name = "address_street")  val addressStreet: String = "",
    @ColumnInfo(name = "address_city")    val addressCity: String = "",
    @ColumnInfo(name = "address_zipcode") val addressZipcode: String = "",

    @ColumnInfo(name = "company_name")    val companyName: String = ""
)
