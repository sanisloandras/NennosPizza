package com.sanislo.nennospizza.domain.usecase.cart

import java.util.*

internal abstract class BaseCartItem(
    val id: String,
    val name: String,
    val price: String,
    val date: Date
)