package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.db.PizzaCartItemEntity
import com.sanislo.nennospizza.presentation.details.PizzaDetails
import java.util.*

class AddPizzaToCartUseCase(private val pizzaCartDao: PizzaCartDao) {

    fun invoke(pizzaDetails: PizzaDetails,
               selectedIngredients: Map<Int, Boolean>
    ) {
        val selectedIngredientIds = selectedIngredients.filterValues { it }.keys
        return pizzaDetails.let {
            val pizzaCartItemEntity = PizzaCartItemEntity(
                id = UUID.randomUUID().toString(),
                name = it.name,
                price = "$${it.price}",
                ingredientIds = selectedIngredientIds,
                createdAt = Date())
            pizzaCartDao.insert(pizzaCartItemEntity)
        }
    }
}