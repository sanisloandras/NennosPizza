package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.DrinkCartItemEntity
import com.sanislo.nennospizza.domain.repository.DrinksRepository
import com.sanislo.nennospizza.presentation.drinks.DrinkListItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class AddDrinkToCartUseCase(private val drinksRepository: DrinksRepository,
                            private val drinkCartDao: DrinkCartDao,
                            private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    suspend fun invoke(drinkListItem: DrinkListItem) {
        withContext(dispatcher) {
            drinksRepository.drinks().firstOrNull { it.id == drinkListItem.id }?.let { drink ->
                DrinkCartItemEntity(
                        UUID.randomUUID().toString(),
                        drink.id,
                        drink.name,
                        "$${drink.price}",
                        Date())
            }?.let { drinkCartDao.insert(it) }
        }
    }
}