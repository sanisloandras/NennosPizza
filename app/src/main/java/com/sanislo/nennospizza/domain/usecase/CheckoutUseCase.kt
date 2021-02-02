package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.api.checkout.Checkout
import com.sanislo.nennospizza.api.checkout.CheckoutService
import com.sanislo.nennospizza.api.checkout.Pizza
import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.PizzaCartDao

class CheckoutUseCase(private val checkoutService: CheckoutService,
                      private val pizzaCartDao: PizzaCartDao,
                      private val drinkCartDao: DrinkCartDao) {

    suspend fun invoke() {
        val pizzas = pizzaCartDao.all().map {
            Pizza(it.ingredientIds.toList(), it.name)
        }
        val drinkIds = drinkCartDao.all().map {
            it.drinkId
        }

        val checkout = Checkout(pizzas, drinkIds)
        checkoutService.checkout(checkout)
        pizzaCartDao.clear()
        drinkCartDao.clear()
    }
}