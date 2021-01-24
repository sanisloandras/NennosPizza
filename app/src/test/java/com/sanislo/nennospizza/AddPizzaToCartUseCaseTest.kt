package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.sanislo.nennospizza.db.PizzaCartDao
import com.sanislo.nennospizza.domain.usecase.AddPizzaToCartUseCase
import com.sanislo.nennospizza.presentation.details.AddToCartState
import com.sanislo.nennospizza.presentation.details.PizzaDetails
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class AddPizzaToCartUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test() = runBlocking{
        val pizzaCartDao = Mockito.mock(PizzaCartDao::class.java)
        val useCase = AddPizzaToCartUseCase(pizzaCartDao)
        useCase.invoke("pizzaName", AddToCartState(), emptySet())
        Mockito.verify(pizzaCartDao).insert(any())
        Mockito.verifyNoMoreInteractions(pizzaCartDao)
    }
}