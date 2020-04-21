package com.sanislo.nennospizza.presentation.details

data class PizzaDetails(
    val name: String,
    val imgUrl: String?,
    val ingredientList: List<IngredientListItem>,
    val initialSelection: Map<Int, Boolean>,
    val price: Double,
    val isAddToCartEnabled: Boolean = true
)