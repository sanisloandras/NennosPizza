package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sanislo.nennospizza.api.data.IngOrDrinkItem
import com.sanislo.nennospizza.api.data.ResourceResponse
import com.sanislo.nennospizza.domain.repository.DrinksRepository
import com.sanislo.nennospizza.domain.usecase.GetDrinksUseCase
import com.sanislo.nennospizza.presentation.drinks.DrinkListItem
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class GetDrinksUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test() {
        runBlocking{
            val drinksRepository = Mockito.mock(DrinksRepository::class.java)
            val response = ResourceResponse()
            response.addAll(listOf(
                IngOrDrinkItem(1, "mock1", 1.0),
                IngOrDrinkItem(2, "mock2", 2.0),
                IngOrDrinkItem(5, "mock5", 3.0))
            )
            Mockito.`when`(drinksRepository.drinks()).thenReturn(response)
            val expected = listOf(
                DrinkListItem(1, "mock1", "$1.0"),
                DrinkListItem(2, "mock2", "$2.0"),
                DrinkListItem(5, "mock5", "$3.0")
            )
            val useCase = GetDrinksUseCase(drinksRepository)
            val drinkList = useCase.invoke()
            Mockito.verify(drinksRepository).drinks()
            Assert.assertEquals(expected, drinkList)
        }
    }
}