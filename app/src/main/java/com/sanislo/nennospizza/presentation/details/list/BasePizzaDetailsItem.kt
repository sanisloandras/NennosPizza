package com.sanislo.nennospizza.presentation.details.list

data class PizzaDetailsHeader(
        val imageUrl: String?,
        val transitionName: String
)

data class IngredientItem(
        val id: Int,
        val name: String,
        val price: String
)