package com.sanislo.nennospizza.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PizzaCartItemEntity::class, DrinkCartItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun pizzaCartDao(): PizzaCartDao
    abstract fun drinkCartDao(): DrinkCartDao
}