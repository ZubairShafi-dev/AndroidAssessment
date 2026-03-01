package com.example.androidassessment.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room [TypeConverter] for [List]<[String]>.
 *
 * Stores the list as a JSON array string in the database column so that
 * sub-breed lists can be persisted without a separate join table.
 *
 * Example stored value: `["boston","english","french"]`
 */
class StringListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        if (value == null) return "[]"
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}
