package com.sanislo.nennospizza.presentation.details

data class PizzaDetails(
        val name: String,
        val imgUrl: String?,
        val ingredients: Set<Int>
)