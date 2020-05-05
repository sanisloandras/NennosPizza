package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository
import com.sanislo.nennospizza.presentation.details.PizzaDetails

class GetPizzaDetailsByNameUseCase(
        private val pizzaRepository: PizzaRepository,
        private val ingredientsRepository: IngredientsRepository) {

    suspend fun invoke(pizzaName: String): PizzaDetails {
        val pizza = pizzaRepository.pizzas().pizzas.first {
            it.name == pizzaName
        }
        return PizzaDetails(
                pizza.name,
                pizza.imageUrl,
                pizza.ingredients.toSet()
        )
    }
}