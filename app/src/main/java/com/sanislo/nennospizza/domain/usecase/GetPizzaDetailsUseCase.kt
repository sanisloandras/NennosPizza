package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository
import com.sanislo.nennospizza.presentation.details.IngredientListItem
import com.sanislo.nennospizza.presentation.details.PizzaDetails
import com.sanislo.nennospizza.presentation.list.PizzaListItem

class GetPizzaDetailsUseCase(private val pizzaRepository: PizzaRepository,
                             private val ingredientsRepository: IngredientsRepository
) {
    suspend fun invoke(pizzaListItem: PizzaListItem): PizzaDetails {
        //todo parallel
        val pizzas = pizzaRepository.pizzas()
        val ingredients = ingredientsRepository.ingredients()
        val pizzasIngredients = ingredients.filter { pizzaListItem.ingredientIds.contains(it.id) }
        val price = pizzas.basePrice + pizzasIngredients.map {
            it.price
        }.reduce { a, b ->
            a + b
        }
        val ingredientList = ingredients.map {
            IngredientListItem(it.id, it.name, "$${it.price}")
        }
        val initialSelection = pizzaListItem.ingredientIds.associateWith { true }
        return PizzaDetails(
            pizzaListItem.name,
            pizzaListItem.imgUrl,
            ingredientList,
            initialSelection,
            price
        )
    }
}