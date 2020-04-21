package com.sanislo.nennospizza.db

import androidx.room.Entity
import androidx.room.TypeConverters
import java.util.*

@Entity(primaryKeys = ["id"])
@TypeConverters(DateConverter::class, SetConverter::class)
data class PizzaCartItemEntity(
    val id: String,
    val name: String,
    val price: String,
    val ingredientIds: Set<Int>,
    val createdAt: Date
)