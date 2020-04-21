package com.sanislo.nennospizza.db

import androidx.room.Entity
import androidx.room.TypeConverters
import java.util.*

@Entity(primaryKeys = ["id"])
@TypeConverters(DateConverter::class, SetConverter::class)
data class DrinkCartItemEntity(
    val id: String,
    val drinkId: Int,
    val name: String,
    val price: String,
    val createdAt: Date
)