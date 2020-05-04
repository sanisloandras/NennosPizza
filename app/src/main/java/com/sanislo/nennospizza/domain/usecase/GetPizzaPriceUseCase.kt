package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository

class GetPizzaPriceUseCase(private val ingredientsRepository: IngredientsRepository, val pizzaRepository: PizzaRepository) {
    suspend fun invoke(ingredientSelection: Set<Int>): Double {
        val ingredients = ingredientsRepository.ingredients()
        val pizzasIngredients = ingredients.filter { ingredientSelection.contains(it.id) }
        return pizzaRepository.pizzas().basePrice + pizzasIngredients.map {
            it.price
        }.reduce { a, b ->
            a + b
        }
    }
}