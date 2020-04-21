package com.sanislo.nennospizza.api.checkout

import com.google.gson.annotations.SerializedName

data class Checkout(
    @SerializedName("pizzas")
    val pizzas: List<Pizza>,
    @SerializedName("drinks")
    val drinks: List<Int>
)