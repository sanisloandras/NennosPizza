package com.sanislo.nennospizza.presentation.list

data class PizzaListItem(
    val name: String,
    val ingredients: String,
    val price: String,
    val imgUrl: String?,
    val ingredientIds: List<Int>
)