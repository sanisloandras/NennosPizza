package com.sanislo.nennospizza.api.data


import com.google.gson.annotations.SerializedName
import com.sanislo.nennospizza.api.data.Pizza

data class PizzasResponse(
    @SerializedName("basePrice")
    val basePrice: Int,
    @SerializedName("pizzas")
    val pizzas: List<Pizza>
)