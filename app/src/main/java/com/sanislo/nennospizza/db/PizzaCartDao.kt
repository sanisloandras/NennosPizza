package com.sanislo.nennospizza.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PizzaCartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pizzaCartItemEntity: PizzaCartItemEntity)

    @Query("SELECT * FROM pizzacartitementity")
    fun all(): List<PizzaCartItemEntity>

    @Query("SELECT * FROM pizzacartitementity")
    fun allFlow(): Flow<List<PizzaCartItemEntity>>

    @Query("DELETE FROM pizzacartitementity WHERE id = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM pizzacartitementity")
    fun clear()
}