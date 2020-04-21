package com.sanislo.nennospizza.presentation.cart.data

import java.util.*

class PizzaCartItem(
    id: String,
    name: String,
    price: String,
    date: Date = Date(),
    val ingredientIds: Set<Int>
) : BaseCartItem(id, name, price, date) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as PizzaCartItem

        if (ingredientIds != other.ingredientIds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ingredientIds.hashCode()
        return result
    }
}