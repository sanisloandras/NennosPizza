package com.sanislo.nennospizza.presentation.cart.data

import java.util.*

class DrinkCartItem(
    id: String,
    name: String,
    price: String,
    date: Date = Date(),
    val drinkId: Int
) : BaseCartItem(id, name, price, date) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as DrinkCartItem
        return drinkId == that.drinkId
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), drinkId)
    }

}