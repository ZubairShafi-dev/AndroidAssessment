package com.example.androidassessment.di.qualifier

import javax.inject.Qualifier

/**
 * Qualifier for the Dog CEO API Retrofit instance.
 * Base URL: https://dog.ceo/api/
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DogApi
