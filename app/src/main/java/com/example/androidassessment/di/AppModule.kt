package com.example.androidassessment.di

import com.example.androidassessment.data.remote.RemoteDataSource
import com.example.androidassessment.data.remote.RemoteDataSourceImpl
import com.example.androidassessment.data.remote.UserRemoteDataSource
import com.example.androidassessment.data.remote.UserRemoteDataSourceImpl
import com.example.androidassessment.data.repository.DogRepositoryImpl
import com.example.androidassessment.data.repository.PostRepositoryImpl
import com.example.androidassessment.data.repository.RandomUserRepositoryImpl
import com.example.androidassessment.data.repository.UserRepositoryImpl
import com.example.androidassessment.domain.repository.DogRepository
import com.example.androidassessment.domain.repository.PostRepository
import com.example.androidassessment.domain.repository.RandomUserRepository
import com.example.androidassessment.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds @Singleton
    abstract fun bindDogRepository(impl: DogRepositoryImpl): DogRepository

    @Binds @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds @Singleton
    abstract fun bindRandomUserRepository(impl: RandomUserRepositoryImpl): RandomUserRepository

    @Binds @Singleton
    abstract fun bindRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource

    @Binds @Singleton
    abstract fun bindUserRemoteDataSource(impl: UserRemoteDataSourceImpl): UserRemoteDataSource
}
