package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.presentation.details.PizzaDetails

class IngredientCheckUseCase(private val ingredientsRepository: IngredientsRepository) {

    suspend fun invoke(pizzaDetails: PizzaDetails?, ingredientId: Int, isSelected: Boolean): PizzaDetails? {
        return pizzaDetails?.let { pizzaDetails ->
            val selectedIngredients = pizzaDetails.initialSelection.toMutableMap()
            if (selectedIngredients[ingredientId] == isSelected) return pizzaDetails
            selectedIngredients[ingredientId] = isSelected
            val newPrice = ingredientsRepository.ingredients().first {
                it.id == ingredientId
            }.let {
                if (isSelected) pizzaDetails.price + it.price else pizzaDetails.price - it.price
            }
            pizzaDetails.copy(
                initialSelection = selectedIngredients,
                price = newPrice
            )
        }
    }
}