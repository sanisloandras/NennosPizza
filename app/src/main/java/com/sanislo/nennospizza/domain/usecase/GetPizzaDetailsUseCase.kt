package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.presentation.details.IngredientListItem
import com.sanislo.nennospizza.presentation.details.PizzaDetails
import com.sanislo.nennospizza.presentation.list.PizzaListItem

class GetPizzaDetailsUseCase(private val ingredientsRepository: IngredientsRepository) {
    suspend fun invoke(pizzaListItem: PizzaListItem): PizzaDetails {
        val ingredientList = ingredientsRepository.ingredients().map {
            IngredientListItem(it.id, it.name, "$${it.price}")
        }
        return PizzaDetails(
            pizzaListItem.name,
                pizzaListItem.imgUrl
        )
    }
}