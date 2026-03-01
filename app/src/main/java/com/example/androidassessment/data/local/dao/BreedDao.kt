package com.example.androidassessment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidassessment.data.local.entity.BreedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Query("SELECT * FROM breeds ORDER BY breed_name ASC")
    fun observeAllBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds ORDER BY breed_name ASC")
    suspend fun getAllBreeds(): List<BreedEntity>

    @Query("SELECT * FROM breeds WHERE breed_name = :breedName LIMIT 1")
    suspend fun getBreedByName(breedName: String): BreedEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedEntity>)

    @Query("DELETE FROM breeds")
    suspend fun clearAll()
}
