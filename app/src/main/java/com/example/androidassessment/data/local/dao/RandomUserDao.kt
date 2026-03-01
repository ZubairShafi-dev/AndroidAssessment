package com.example.androidassessment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidassessment.data.local.entity.RandomUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomUserDao {

    @Query("SELECT * FROM random_users ORDER BY full_name ASC")
    fun observeAllRandomUsers(): Flow<List<RandomUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<RandomUserEntity>)

    @Query("DELETE FROM random_users")
    suspend fun clearAll()
}
