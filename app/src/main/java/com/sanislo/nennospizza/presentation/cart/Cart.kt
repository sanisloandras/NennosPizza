package com.sanislo.nennospizza.presentation.cart

import com.sanislo.nennospizza.presentation.cart.adapter.CartListItem

data class Cart(
    val cartItems: List<CartListItem> = emptyList(),
    val price: Double = 0.0
)