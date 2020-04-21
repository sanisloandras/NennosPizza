package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.presentation.cart.data.BaseCartItem
import com.sanislo.nennospizza.presentation.cart.data.DrinkCartItem
import com.sanislo.nennospizza.presentation.cart.data.PizzaCartItem

class RemoveFromCartUseCase(private val pizzaCartDao: PizzaCartDao,
                            private val drinkCartDao: DrinkCartDao) {

    fun invoke(baseCartItem: BaseCartItem) {
        when (baseCartItem) {
            is PizzaCartItem -> {
                pizzaCartDao.deleteById(baseCartItem.id)
            }
            is DrinkCartItem -> {
                drinkCartDao.deleteById(baseCartItem.id)
            }
        }
    }
}