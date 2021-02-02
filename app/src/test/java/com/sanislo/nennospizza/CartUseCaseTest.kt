package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.db.DrinkCartItemEntity
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.db.PizzaCartItemEntity
import com.sanislo.nennospizza.domain.usecase.cart.CartUseCase
import com.sanislo.nennospizza.presentation.cart.Cart
import com.sanislo.nennospizza.presentation.cart.adapter.CartListItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import java.util.*

class CartUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test() = runBlocking {
        val pizzaCartDao = Mockito.mock(PizzaCartDao::class.java)
        val drinkCartDao = Mockito.mock(DrinkCartDao::class.java)
        val date = Date()
        `when`(pizzaCartDao.allFlow()).thenReturn(flowOf(listOf(PizzaCartItemEntity("1", "mockPizza", "$1.0", setOf(1, 2), date))))
        `when`(drinkCartDao.allFlow()).thenReturn(flowOf(listOf(DrinkCartItemEntity("2", 2, "mockDrink", "$2.0", date))))
        val useCase = CartUseCase(pizzaCartDao, drinkCartDao)
        val cartFlow = useCase.invoke()
        val cartListItems = listOf(
                CartListItem("1", "mockPizza", "$1.0"),
                CartListItem("2", "mockDrink", "$2.0")
        )
        val expected = Cart(cartListItems, 3.0)
        cartFlow.collect {
            Assert.assertEquals(it, expected)
            Mockito.verify(pizzaCartDao, times(1)).allFlow()
            Mockito.verify(drinkCartDao, times(1)).allFlow()
        }
    }
}