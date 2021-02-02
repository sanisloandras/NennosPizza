package com.sanislo.nennospizza.domain.usecase.cart

import java.util.*

internal class PizzaCartItem(
        id: String,
        name: String,
        price: String,
        date: Date = Date(),
        val ingredientIds: Set<Int>
) : BaseCartItem(id, name, price, date)