package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.domain.usecase.RemoveFromCartUseCase
import com.sanislo.nennospizza.presentation.cart.data.DrinkCartItem
import com.sanislo.nennospizza.presentation.cart.data.PizzaCartItem
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class RemoveFromCartUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val pizzaCartDao = Mockito.mock(PizzaCartDao::class.java)
    private val drinkCartDao = Mockito.mock(DrinkCartDao::class.java)
    private val useCase = RemoveFromCartUseCase(pizzaCartDao, drinkCartDao)

    @Test
    fun testPizzaCartItem() = runBlocking{
        val cartItem = PizzaCartItem("1", "mock", "mock", ingredientIds = emptySet())
        useCase.invoke(cartItem)
        Mockito.verify(pizzaCartDao).deleteById("1")
        Mockito.verifyNoMoreInteractions(pizzaCartDao)
        Mockito.verifyNoMoreInteractions(drinkCartDao)
    }

    @Test
    fun testDrinkCartItem() = runBlocking{
        val cartItem = DrinkCartItem("1", "mock", "mock", drinkId = 1)
        useCase.invoke(cartItem)
        Mockito.verify(drinkCartDao).deleteById("1")
        Mockito.verifyNoMoreInteractions(pizzaCartDao)
        Mockito.verifyNoMoreInteractions(drinkCartDao)
    }
}