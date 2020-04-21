package com.sanislo.nennospizza.api.data


import com.google.gson.annotations.SerializedName

data class Pizza(
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("ingredients")
    val ingredients: List<Int>,
    @SerializedName("name")
    val name: String
)