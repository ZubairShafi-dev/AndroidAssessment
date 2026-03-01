package com.example.androidassessment.di.qualifier

import javax.inject.Qualifier

/**
 * Qualifier for the JSONPlaceholder Users API Retrofit instance.
 * Base URL: https://jsonplaceholder.typicode.com/
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersApi
