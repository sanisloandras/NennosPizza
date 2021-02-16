package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.DrinksRepository
import com.sanislo.nennospizza.presentation.drinks.DrinkListItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetDrinksUseCase(private val drinksRepository: DrinksRepository,
                       private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun invoke(): List<DrinkListItem> {
        return withContext(dispatcher) {
            drinksRepository.drinks().map {
                DrinkListItem(it.id, it.name, "$${it.price}")
            }
        }
    }
}