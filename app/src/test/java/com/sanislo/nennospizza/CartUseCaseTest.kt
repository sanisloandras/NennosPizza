package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.sanislo.nennospizza.api.checkout.CheckoutService
import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.domain.usecase.CartUseCase
import com.sanislo.nennospizza.domain.usecase.CheckoutUseCase
import com.sanislo.nennospizza.presentation.cart.data.Cart
import com.sanislo.nennospizza.presentation.cart.data.DrinkCartItem
import com.sanislo.nennospizza.presentation.cart.data.PizzaCartItem
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class CartUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test() = runBlocking{
        val pizzaCartDao = Mockito.mock(PizzaCartDao::class.java)
        val drinkCartDao = Mockito.mock(DrinkCartDao::class.java)

        val useCase = CartUseCase(pizzaCartDao, drinkCartDao)
        val cartItems = listOf(
            PizzaCartItem("1", "mock", "mock", Date(), setOf(1,2)),
            DrinkCartItem("1", "mock", "mock", Date(), 1)
        )
        val cartFlow =  useCase.invoke()
        cartFlow.
        Mockito.verify(checkoutService, times(1)).checkout(any())
        Mockito.verify(pizzaCartDao, times(1)).clear()
        Mockito.verify(drinkCartDao, times(1)).clear()
    }
}