package com.example.androidassessment.data.mock

import com.example.androidassessment.data.local.entity.BreedEntity
import javax.inject.Inject

class MockDogDataSource @Inject constructor() {

    fun getBreedEntities(): List<BreedEntity> = listOf(
        BreedEntity(
            breedName = "bulldog",
            subBreeds = listOf("boston", "english", "french")
        ),
        BreedEntity(
            breedName = "poodle",
            subBreeds = listOf("miniature", "standard", "toy")
        ),
        BreedEntity(
            breedName = "retriever",
            subBreeds = listOf("chesapeake", "curly", "flatcoated", "golden")
        ),
        BreedEntity(
            breedName = "spaniel",
            subBreeds = listOf("blenheim", "cocker", "irish", "japanese", "sussex", "welsh")
        ),
        BreedEntity(
            breedName = "terrier",
            subBreeds = listOf("american", "bedlington", "border", "fox", "scottish", "yorkshire")
        ),
        BreedEntity(
            breedName = "hound",
            subBreeds = listOf("afghan", "basset", "blood", "english", "greyhound", "ibizan")
        ),
        BreedEntity(
            breedName = "shepherd",
            subBreeds = listOf("australian", "english", "german", "shetland")
        ),
        BreedEntity(breedName = "labrador",    subBreeds = emptyList()),
        BreedEntity(breedName = "beagle",      subBreeds = emptyList()),
        BreedEntity(breedName = "boxer",       subBreeds = emptyList()),
        BreedEntity(breedName = "chihuahua",   subBreeds = emptyList()),
        BreedEntity(breedName = "dalmation",   subBreeds = emptyList()),
        BreedEntity(breedName = "doberman",    subBreeds = emptyList()),
        BreedEntity(breedName = "husky",       subBreeds = emptyList()),
        BreedEntity(breedName = "rottweiler",  subBreeds = emptyList())
    )
}
