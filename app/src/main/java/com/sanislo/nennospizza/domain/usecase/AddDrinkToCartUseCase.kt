package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.DrinkCartItemEntity
import com.sanislo.nennospizza.domain.repository.DrinksRepository
import com.sanislo.nennospizza.presentation.drinks.DrinkListItem
import java.util.*

class AddDrinkToCartUseCase(private val drinksRepository: DrinksRepository,
                            private val drinkCartDao: DrinkCartDao) {
    suspend fun invoke(drinkListItem: DrinkListItem) {
        val drink = drinksRepository.drinks().first { it.id == drinkListItem.id }
        val drinkCartItemEntity = DrinkCartItemEntity(
            UUID.randomUUID().toString(),
            drink.id,
            drink.name,
            "$${drink.price}",
            Date())
        drinkCartDao.insert(drinkCartItemEntity)
    }
}