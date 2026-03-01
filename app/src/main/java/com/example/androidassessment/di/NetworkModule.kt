package com.example.androidassessment.di

import com.example.androidassessment.BuildConfig
import com.example.androidassessment.data.remote.dog.DogApiService
import com.example.androidassessment.data.remote.posts.PostsApiService
import com.example.androidassessment.data.remote.randomuser.RandomUserApiService
import com.example.androidassessment.data.remote.users.UsersApiService
import com.example.androidassessment.di.qualifier.DogApi
import com.example.androidassessment.di.qualifier.RandomUserApi
import com.example.androidassessment.di.qualifier.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val DOG_BASE_URL      = "https://dog.ceo/api/"
    private const val USERS_BASE_URL   = "https://jsonplaceholder.typicode.com/"
    private const val RANDOM_USER_URL  = "https://randomuser.me/"

    private const val TIMEOUT_SECONDS = 30L

    // ── OkHttpClient ──────────────────────────────────────────────────────────

    /**
     * Shared [OkHttpClient] used by both Retrofit instances.
     *
     * [HttpLoggingInterceptor] is added **only** when
     * [BuildConfig.IS_LOGGING_ENABLED] is true, so no request/response
     * details ever appear in prod/release builds.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

        if (BuildConfig.IS_LOGGING_ENABLED) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }

        return builder.build()
    }

    // ── Retrofit instances ────────────────────────────────────────────────────

    /** Retrofit wired to `https://dog.ceo/api/`. */
    @Provides
    @Singleton
    @DogApi
    fun provideDogRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(DOG_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /** Retrofit wired to `https://jsonplaceholder.typicode.com/`. */
    @Provides
    @Singleton
    @UsersApi
    fun provideUsersRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(USERS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /** Retrofit wired to `https://randomuser.me/`. */
    @Provides
    @Singleton
    @RandomUserApi
    fun provideRandomUserRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(RANDOM_USER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // ── API Services ──────────────────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideDogApiService(@DogApi retrofit: Retrofit): DogApiService =
        retrofit.create(DogApiService::class.java)

    @Provides
    @Singleton
    fun provideUsersApiService(@UsersApi retrofit: Retrofit): UsersApiService =
        retrofit.create(UsersApiService::class.java)

    @Provides
    @Singleton
    fun providePostsApiService(@UsersApi retrofit: Retrofit): PostsApiService =
        retrofit.create(PostsApiService::class.java)

    @Provides
    @Singleton
    fun provideRandomUserApiService(@RandomUserApi retrofit: Retrofit): RandomUserApiService =
        retrofit.create(RandomUserApiService::class.java)
}
