package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.sanislo.nennospizza.api.checkout.Checkout
import com.sanislo.nennospizza.api.checkout.CheckoutService
import com.sanislo.nennospizza.api.checkout.Pizza
import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.DrinkCartItemEntity
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.db.PizzaCartItemEntity
import com.sanislo.nennospizza.domain.usecase.CheckoutUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class CheckoutUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test() = runBlocking {
        val checkoutService = Mockito.mock(CheckoutService::class.java)

        val pizzaCartDao = Mockito.mock(PizzaCartDao::class.java)
        Mockito.`when`(pizzaCartDao.all()).thenReturn(
                listOf(
                        PizzaCartItemEntity("1", "pizza_1", "$3.0", setOf(1, 2), Date()),
                        PizzaCartItemEntity("2", "pizza_2", "$4.0", setOf(2, 3), Date()),
                )
        )

        val drinkCartDao = Mockito.mock(DrinkCartDao::class.java)
        Mockito.`when`(drinkCartDao.all()).thenReturn(
                listOf(DrinkCartItemEntity("3", 7, "drink_1", "$1.0", Date()))
        )

        val useCase = CheckoutUseCase(checkoutService, pizzaCartDao, drinkCartDao)
        useCase.invoke()

        val checkout = Checkout(
                listOf(
                        Pizza(listOf(1, 2), "pizza_1"),
                        Pizza(listOf(2, 3), "pizza_2"),
                ),
                listOf(7)
        )

        Mockito.verify(checkoutService, times(1)).checkout(checkout)
        Mockito.verify(pizzaCartDao, times(1)).clear()
        Mockito.verify(drinkCartDao, times(1)).clear()
    }
}