package com.sanislo.nennospizza.presentation.details

data class IngredientListState(
        val ingredientList: List<IngredientListItem>,
        val selection: Set<Int>
)