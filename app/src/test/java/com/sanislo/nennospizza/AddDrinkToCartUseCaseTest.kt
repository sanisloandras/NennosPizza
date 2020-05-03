package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.sanislo.nennospizza.db.DrinkCartDao
import com.sanislo.nennospizza.domain.repository.DrinksRepositoryImpl
import com.sanislo.nennospizza.domain.usecase.AddDrinkToCartUseCase
import com.sanislo.nennospizza.presentation.drinks.DrinkListItem
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class AddDrinkToCartUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test() = runBlocking{
        val drinksResponse = DrinksRepositoryImpl(FakeDataService())
        val drinkCartDao = Mockito.mock(DrinkCartDao::class.java)
        val addDrinkToCartUseCase = AddDrinkToCartUseCase(drinksResponse, drinkCartDao)
        addDrinkToCartUseCase.invoke(DrinkListItem(1, "mock", "$5.0"))
        Mockito.verify(drinkCartDao).insert(any())
        addDrinkToCartUseCase.invoke(DrinkListItem(9999, "mock", "$5.0"))
        Mockito.verifyNoMoreInteractions(drinkCartDao)
    }
}