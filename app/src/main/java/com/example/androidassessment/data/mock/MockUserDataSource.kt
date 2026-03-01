package com.example.androidassessment.data.mock

import com.example.androidassessment.data.local.entity.UserEntity
import javax.inject.Inject

/**
 * Hard-coded data source used when [BuildConfig.USE_MOCK_DATA] is true.
 * Returns 5 sample users matching JSONPlaceholder structure.
 */
class MockUserDataSource @Inject constructor() {

    fun getUserEntities(): List<UserEntity> = listOf(
        UserEntity(
            id = 1,
            name = "Leanne Graham",
            username = "Bret",
            email = "Sincere@april.biz",
            phone = "1-770-736-8031 x56442",
            website = "hildegard.org",
            addressStreet = "Kulas Light",
            addressCity = "Gwenborough",
            addressZipcode = "92998-3874",
            companyName = "Romaguera-Crona"
        ),
        UserEntity(
            id = 2,
            name = "Ervin Howell",
            username = "Antonette",
            email = "Shanna@melissa.tv",
            phone = "010-692-6593 x09125",
            website = "anastasia.net",
            addressStreet = "Victor Plains",
            addressCity = "Wisokyburgh",
            addressZipcode = "90566-7771",
            companyName = "Deckow-Crist"
        ),
        UserEntity(
            id = 3,
            name = "Clementine Bauch",
            username = "Samantha",
            email = "Nathan@yesenia.net",
            phone = "1-463-123-4447",
            website = "ramiro.info",
            addressStreet = "Douglas Extension",
            addressCity = "McKenziehaven",
            addressZipcode = "59590-4157",
            companyName = "Romaguera-Jacobson"
        ),
        UserEntity(
            id = 4,
            name = "Patricia Lebsack",
            username = "Karianne",
            email = "Julianne.OConner@kory.org",
            phone = "493-170-9623 x156",
            website = "kale.biz",
            addressStreet = "Hoeger Mall",
            addressCity = "South Elvis",
            addressZipcode = "53919-4257",
            companyName = "Robel-Corkery"
        ),
        UserEntity(
            id = 5,
            name = "Chelsey Dietrich",
            username = "Kamren",
            email = "Lucio_Hettinger@annie.ca",
            phone = "(254)954-1289",
            website = "demarco.info",
            addressStreet = "Skiles Walks",
            addressCity = "Roscoeview",
            addressZipcode = "33263",
            companyName = "Keebler LLC"
        )
    )
}
