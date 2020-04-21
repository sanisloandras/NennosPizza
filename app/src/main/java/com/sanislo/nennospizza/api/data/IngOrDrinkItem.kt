package com.sanislo.nennospizza.api.data

import com.google.gson.annotations.SerializedName

data class IngOrDrinkItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double
)