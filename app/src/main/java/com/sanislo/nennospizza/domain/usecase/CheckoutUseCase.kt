package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.api.checkout.Checkout
import com.sanislo.nennospizza.api.checkout.CheckoutService
import com.sanislo.nennospizza.api.checkout.Pizza
import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.presentation.cart.data.Cart
import com.sanislo.nennospizza.presentation.cart.data.DrinkCartItem
import com.sanislo.nennospizza.presentation.cart.data.PizzaCartItem

class CheckoutUseCase(private val checkoutService: CheckoutService,
                      private val pizzaCartDao: PizzaCartDao,
                      private val drinkCartDao: DrinkCartDao) {

    suspend fun invoke(cart: Cart) {
        val pizzas = cart.cartItems
            .filterIsInstance(PizzaCartItem::class.java)
            .map {
                Pizza(it.ingredientIds.toList(), it.name)
            }
        val drinkIds = cart.cartItems.filterIsInstance(DrinkCartItem::class.java)
            .map {
                it.drinkId
            }
        val checkout = Checkout(pizzas, drinkIds)
        try {
            checkoutService.checkout(checkout)
            pizzaCartDao.clear()
            drinkCartDao.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}