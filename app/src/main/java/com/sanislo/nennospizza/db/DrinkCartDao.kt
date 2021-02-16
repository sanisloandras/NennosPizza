package com.sanislo.nennospizza.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkCartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drinkCartItemEntity: DrinkCartItemEntity)

    @Query("SELECT * FROM drinkcartitementity")
    suspend fun all(): List<DrinkCartItemEntity>

    @Query("SELECT * FROM drinkcartitementity")
    fun allFlow(): Flow<List<DrinkCartItemEntity>>

    @Query("DELETE FROM drinkcartitementity WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM drinkcartitementity")
    suspend fun clear()
}