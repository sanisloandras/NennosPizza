package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository
import com.sanislo.nennospizza.presentation.details.IngredientListItem
import com.sanislo.nennospizza.presentation.details.PizzaDetails

class GetPizzaDetailsByNameUseCase(
        private val pizzaRepository: PizzaRepository,
        private val ingredientsRepository: IngredientsRepository) {

    suspend fun invoke(pizzaName: String): PizzaDetails {
        val pizza = pizzaRepository.pizzas().pizzas.first {
            it.name == pizzaName
        }
        val ingredientList = ingredientsRepository.ingredients().map {
            IngredientListItem(it.id, it.name, "$${it.price}")
        }
        return PizzaDetails(
                pizza.name,
                pizza.imageUrl,
                ingredientList
        )
    }
}