package com.example.androidassessment.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidassessment.data.local.converter.StringListConverter
import com.example.androidassessment.data.local.dao.BreedDao
import com.example.androidassessment.data.local.dao.PostDao
import com.example.androidassessment.data.local.dao.RandomUserDao
import com.example.androidassessment.data.local.dao.UserDao
import com.example.androidassessment.data.local.entity.BreedEntity
import com.example.androidassessment.data.local.entity.PostEntity
import com.example.androidassessment.data.local.entity.RandomUserEntity
import com.example.androidassessment.data.local.entity.UserEntity

@Database(
    entities = [
        BreedEntity::class,
        UserEntity::class,
        PostEntity::class,
        RandomUserEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breedDao(): BreedDao
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun randomUserDao(): RandomUserDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}
