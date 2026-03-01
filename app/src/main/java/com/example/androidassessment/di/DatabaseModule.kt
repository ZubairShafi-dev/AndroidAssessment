package com.example.androidassessment.di

import android.content.Context
import com.example.androidassessment.data.local.dao.BreedDao
import com.example.androidassessment.data.local.dao.PostDao
import com.example.androidassessment.data.local.dao.RandomUserDao
import com.example.androidassessment.data.local.dao.UserDao
import com.example.androidassessment.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import androidx.room.Room
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideBreedDao(db: AppDatabase): BreedDao = db.breedDao()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun providePostDao(db: AppDatabase): PostDao = db.postDao()

    @Provides
    @Singleton
    fun provideRandomUserDao(db: AppDatabase): RandomUserDao = db.randomUserDao()
}
