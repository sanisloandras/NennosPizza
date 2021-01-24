package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository
import com.sanislo.nennospizza.presentation.details.PizzaDetailsInput
import com.sanislo.nennospizza.presentation.details.PizzaDetailsViewModel
import com.sanislo.nennospizza.presentation.details.list.IngredientItem
import com.sanislo.nennospizza.presentation.details.list.PizzaDetailsHeader

class GetPizzaDetailsUseCase(
        private val pizzaRepository: PizzaRepository,
        private val ingredientsRepository: IngredientsRepository
) {
    suspend fun invoke(input: PizzaDetailsInput, userIngredientSelection: Set<Int>?): PizzaDetailsViewModel.PizzaDetailsState {
        val pizzas = pizzaRepository.pizzas()
        val pizza = pizzas.pizzas.first { it.name == input.pizzaName }
        val ingredients = ingredientsRepository.ingredients()
        val ingredientItems = mutableListOf<IngredientItem>()
        val selection = userIngredientSelection ?: pizza.ingredients.toSet()
        var initialPrice = pizzas.basePrice
        //non-reactive but efficient
        ingredients.forEach {
            ingredientItems.add(IngredientItem(it.id, it.name, "$${it.price}"))
            if (selection.contains(it.id)) initialPrice += it.price
        }
        return PizzaDetailsViewModel.PizzaDetailsState(
                PizzaDetailsHeader(input.imgUrl, input.transitionName),
                ingredientItems,
                selection, initialPrice)
    }
}