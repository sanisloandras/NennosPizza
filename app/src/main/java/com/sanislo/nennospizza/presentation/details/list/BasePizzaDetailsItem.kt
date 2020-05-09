package com.sanislo.nennospizza.presentation.details.list

abstract class BasePizzaDetailsItem {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class PizzaImageItem(
        val imageUrl: String?,
        val transitionName: String
) : BasePizzaDetailsItem()

class IngredientItem(
        val id: Int,
        val name: String,
        val price: String
) : BasePizzaDetailsItem()