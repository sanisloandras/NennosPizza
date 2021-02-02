package com.sanislo.nennospizza

import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.domain.usecase.RemoveFromCartUseCase
import com.sanislo.nennospizza.presentation.cart.adapter.CartListItem
import org.junit.Test
import org.mockito.Mockito

class RemoveFromCartUseCaseTest {
    private val pizzaCartDao = Mockito.mock(PizzaCartDao::class.java)
    private val drinkCartDao = Mockito.mock(DrinkCartDao::class.java)
    private val useCase = RemoveFromCartUseCase(pizzaCartDao, drinkCartDao)

    @Test
    fun test() {
        val cartItem = CartListItem("1", "Coca cola", "$1.0")
        useCase.invoke(cartItem)
        Mockito.verify(pizzaCartDao).deleteById("1")
        Mockito.verify(drinkCartDao).deleteById("1")
    }
}