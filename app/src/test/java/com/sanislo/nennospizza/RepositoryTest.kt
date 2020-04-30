package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.api.data.IngOrDrinkResponse
import com.sanislo.nennospizza.api.data.PizzasResponse
import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.domain.repository.*
import com.sanislo.nennospizza.domain.usecase.AddDrinkToCartUseCase
import com.sanislo.nennospizza.presentation.drinks.DrinkListItem
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


class RepositoryTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testPizzaRep() {
        runBlocking{
            val dataService = Mockito.mock(DataService::class.java)
            Mockito.`when`(dataService.pizzas()).thenReturn(PizzasResponse(0, emptyList()))
            val repository = PizzaRepositoryImpl(dataService)
            repository.pizzas()
            repository.pizzas()
            Mockito.verify(dataService, times(1)).pizzas()
        }
    }

    @Test
    fun testIngredientsRep() {
        runBlocking{
            val dataService = Mockito.mock(DataService::class.java)
            Mockito.`when`(dataService.ingredients()).thenReturn(IngOrDrinkResponse())
            val repository = IngredientsRepositoryImpl(dataService)
            repository.ingredients()
            repository.ingredients()
            Mockito.verify(dataService, times(1)).ingredients()
        }
    }

    @Test
    fun testDrinksRep() {
        runBlocking{
            val dataService = Mockito.mock(DataService::class.java)
            Mockito.`when`(dataService.drinks()).thenReturn(IngOrDrinkResponse())
            val repository = DrinksRepositoryImpl(dataService)
            repository.drinks()
            repository.drinks()
            Mockito.verify(dataService, times(1)).drinks()
        }
    }
}