package com.sanislo.nennospizza.domain.usecase.cart

import java.util.*

internal class DrinkCartItem(
    id: String,
    name: String,
    price: String,
    date: Date = Date(),
    val drinkId: Int
) : BaseCartItem(id, name, price, date)