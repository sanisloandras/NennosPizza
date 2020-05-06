package com.sanislo.nennospizza.presentation.details

data class AddToCartState(
        val isLoaded: Boolean = false,
        val isEnabled: Boolean = false,
        val price: Double = 0.0
)