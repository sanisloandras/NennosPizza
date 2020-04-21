package com.sanislo.nennospizza.presentation.cart.data

import java.util.*

abstract class BaseCartItem(
    val id: String,
    val name: String,
    val price: String,
    val date: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseCartItem

        if (id != other.id) return false
        if (name != other.name) return false
        if (price != other.price) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}