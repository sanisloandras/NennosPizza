package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.DrinksRepository
import com.sanislo.nennospizza.presentation.drinks.DrinkListItem

class GetDrinksUseCase(private val drinksRepository: DrinksRepository) {

    suspend fun invoke(): List<DrinkListItem> {
        return drinksRepository.drinks().map {
            DrinkListItem(it.id, it.name, "$${it.price}")
        }
    }
}