package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository

class GetPizzaPriceChangeUseCase(private val ingredientsRepository: IngredientsRepository) {
    suspend fun invoke(id: Int, isSelected: Boolean): Double {
        val ingredientPrice = ingredientsRepository.ingredients().first { it.id == id }.price
        return if (isSelected) ingredientPrice else ingredientPrice * -1
    }
}