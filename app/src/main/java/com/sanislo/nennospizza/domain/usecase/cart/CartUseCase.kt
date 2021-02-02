package com.sanislo.nennospizza.domain.usecase.cart

import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.presentation.cart.Cart
import com.sanislo.nennospizza.presentation.cart.adapter.CartListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class CartUseCase(private val pizzaCartDao: PizzaCartDao,
                  private val drinkCartDao: DrinkCartDao) {

    /**
     * The state of the "Cart" is persisted accross app restarts. This use case return a stream of the state of the current "Cart".
     * todo short explenation why is this so overcomplicated
     * 1) in the design it's not explained what to do when, for example, a drink is added multiple times
     * 2) pizzas have no id at all
     * 3) I did not want to mix pizza and drink database entity/records
     * the current behaviour is: if an item is added multiple times, it shows multiple times in the list
     * for this to work, every record in the database has a unique id generated using UUID
     * to preserve the order of items in the cart, we sort by the date they were added
     */
    fun invoke(): Flow<Cart> {
        val pizzaCartItems = pizzaCartDao.allFlow().map {
            it.map { pizzaCartItemEntity ->
                PizzaCartItem(pizzaCartItemEntity.id,
                    pizzaCartItemEntity.name,
                    pizzaCartItemEntity.price,
                    pizzaCartItemEntity.createdAt,
                    pizzaCartItemEntity.ingredientIds)
            }
        }
        val drinkCartItems = drinkCartDao.allFlow().map {
            it.map { drinkCartItemEntity ->
                DrinkCartItem(drinkCartItemEntity.id,
                    drinkCartItemEntity.name,
                    drinkCartItemEntity.price,
                    drinkCartItemEntity.createdAt,
                    drinkCartItemEntity.drinkId)
            }
        }

        return combine(pizzaCartItems, drinkCartItems) { pizzas, drinks ->
            val cartItems = pizzas.plus(drinks).sortedBy { it.date }
            val price = if (cartItems.isEmpty()) 0.0 else cartItems.map {
                it.price.substringAfter("$").toDouble()
            }.reduce { a, b ->
                return@reduce a + b
            }
            Cart(cartItems.map { CartListItem(it.id, it.name, it.price) }, price)
        }
    }
}