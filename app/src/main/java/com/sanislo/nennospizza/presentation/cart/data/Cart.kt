package com.sanislo.nennospizza.presentation.cart.data

data class Cart(
    val cartItems: List<BaseCartItem> = emptyList(),
    val price: Double = 0.0
)