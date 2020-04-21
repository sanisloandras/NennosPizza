package com.sanislo.nennospizza.api.checkout

import com.google.gson.annotations.SerializedName

data class Pizza(
    @SerializedName("ingredients")
    val ingredients: List<Int>,
    @SerializedName("name")
    val name: String
)