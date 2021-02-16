package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.presentation.cart.adapter.CartListItem

class RemoveFromCartUseCase(private val pizzaCartDao: PizzaCartDao,
                            private val drinkCartDao: DrinkCartDao) {

    suspend fun invoke(cartListItem: CartListItem) {
        pizzaCartDao.deleteById(cartListItem.id)
        drinkCartDao.deleteById(cartListItem.id)
    }
}