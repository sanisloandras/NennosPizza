package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.db.PizzaCartItemEntity
import com.sanislo.nennospizza.presentation.details.AddToCartState
import java.util.*

class AddPizzaToCartUseCase(private val pizzaCartDao: PizzaCartDao) {

    fun invoke(pizzaName: String,
               addToCartState: AddToCartState,
               selectedIngredientIds: Set<Int>
    ) {
        val pizzaCartItemEntity = PizzaCartItemEntity(
                id = UUID.randomUUID().toString(),
                name = pizzaName,
                price = "${addToCartState.price}",
                ingredientIds = selectedIngredientIds,
                createdAt = Date())
        pizzaCartDao.insert(pizzaCartItemEntity)
    }
}