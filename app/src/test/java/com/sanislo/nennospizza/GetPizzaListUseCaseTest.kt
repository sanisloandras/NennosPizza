package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.sanislo.nennospizza.api.data.IngOrDrinkItem
import com.sanislo.nennospizza.api.data.IngOrDrinkResponse
import com.sanislo.nennospizza.api.data.Pizza
import com.sanislo.nennospizza.api.data.PizzasResponse
import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository
import com.sanislo.nennospizza.domain.usecase.GetPizzaListUseCase
import com.sanislo.nennospizza.presentation.list.PizzaListItem
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class GetPizzaListUseCaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    //todo proper way to test this would be to mock the response, but I dont't have time for that, sorry :/
    @Test
    fun test() {
        runBlocking{
            val pizzaRepository = Mockito.mock(PizzaRepository::class.java)
            val pizzas = listOf(
                Pizza("mockImgUrl", listOf(1,2), "mockPizza"),
                Pizza("anotherMockImgUrl", listOf(1,5), "anotherMockPizza")
            )
            val pizzasResponse = PizzasResponse(4, pizzas)
            Mockito.`when`(pizzaRepository.pizzas()).thenReturn(pizzasResponse)

            val ingredientsRepository = Mockito.mock(IngredientsRepository::class.java)
            val ingredientsReponse = IngOrDrinkResponse()
            ingredientsReponse.addAll(listOf(
                IngOrDrinkItem(1, "mockIngredient1", 1.0),
                IngOrDrinkItem(2, "mockIngredient2", 2.0),
                IngOrDrinkItem(5, "mockIngredient5", 3.0))
            )
            Mockito.`when`(ingredientsRepository.ingredients()).thenReturn(ingredientsReponse)

            val getPizzaListUseCase = GetPizzaListUseCase(pizzaRepository, ingredientsRepository)
            val pizzaList = getPizzaListUseCase.invoke()
            val expectedPizzaList = listOf(
                PizzaListItem("mockPizza", "mockIngredient1, mockIngredient2", "$7.0", "mockImgUrl", listOf(1,2)),
                PizzaListItem("anotherMockPizza", "mockIngredient1, mockIngredient5", "$8.0", "anotherMockImgUrl", listOf(1,5))
            )
            Assert.assertEquals(expectedPizzaList, pizzaList)
            Mockito.verify(pizzaRepository, times(1)).pizzas()
            Mockito.verify(ingredientsRepository, times(1)).ingredients()
        }
    }
}