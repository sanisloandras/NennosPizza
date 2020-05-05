package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.presentation.details.IngredientListItem

class GetIngredientsUseCase(private val ingredientsRepository: IngredientsRepository) {

    suspend fun invoke(): List<IngredientListItem> {
        return ingredientsRepository.ingredients().map {
            IngredientListItem(it.id, it.name, "$${it.price}")
        }
    }
}