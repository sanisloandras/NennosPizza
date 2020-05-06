package com.sanislo.nennospizza.api.data


import com.google.gson.annotations.SerializedName

data class PizzasResponse(
        @SerializedName("basePrice")
        val basePrice: Double,
        @SerializedName("pizzas")
    val pizzas: List<Pizza>
)