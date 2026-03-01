package com.example.androidassessment.data.mock

import com.example.androidassessment.data.model.RandomUserResponse
import com.example.androidassessment.data.model.RandomUserResult
import com.example.androidassessment.data.model.RandomUserName
import javax.inject.Inject

class MockRandomUserDataSource @Inject constructor() {

    fun getRandomUsers(): RandomUserResponse = RandomUserResponse(
        results = listOf(
            RandomUserResult(RandomUserName("John", "Doe"), "john.doe@example.com", "555-0101", null),
            RandomUserResult(RandomUserName("Jane", "Smith"), "jane.smith@example.com", "555-0102", null),
            RandomUserResult(RandomUserName("Bob", "Johnson"), "bob.j@example.com", "555-0103", null),
            RandomUserResult(RandomUserName("Alice", "Williams"), "alice.w@example.com", "555-0104", null),
            RandomUserResult(RandomUserName("Charlie", "Brown"), "charlie.b@example.com", "555-0105", null)
        )
    )
}
