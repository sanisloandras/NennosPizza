package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.sanislo.nennospizza.domain.usecase.AddDrinkToCartUseCase
import com.sanislo.nennospizza.domain.usecase.GetDrinksUseCase
import com.sanislo.nennospizza.presentation.drinks.DrinkListItem
import com.sanislo.nennospizza.presentation.drinks.DrinkListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.time.measureDuration
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DrinkListVmTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var mockObserver: Observer<List<DrinkListItem>>

    @Mock
    private lateinit var getDrinksUseCase: GetDrinksUseCase

    @Mock
    private lateinit var addDrinkToCartUseCase: AddDrinkToCartUseCase

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<List<DrinkListItem>>

    @Mock
    private lateinit var drinkAddedToCartObserver: Observer<Boolean>

    @Captor
    private lateinit var drinkAddedToCartCaptor: ArgumentCaptor<Boolean>

    private lateinit var viewModel: DrinkListViewModel

    private val drinkListItems = listOf(
            DrinkListItem(1, "Coca Cola", "$1.25"),
            DrinkListItem(2, "Fanta", "$0.99")
    )

    @Before
    fun setupViewModel() {
        viewModel = DrinkListViewModel(mainCoroutineRule.dispatcher, getDrinksUseCase, addDrinkToCartUseCase)
    }

    @Test
    fun test_drinks() = mainCoroutineRule.runBlockingTest {
        Mockito.`when`(getDrinksUseCase.invoke()).thenReturn(drinkListItems)
        viewModel.drinks.observeForever(mockObserver)
        verify(mockObserver, times(1)).onChanged(argumentCaptor.capture())
        assertEquals(argumentCaptor.value, drinkListItems)

        viewModel.addDrink(drinkListItems[0])
        verify(addDrinkToCartUseCase, times(1)).invoke(drinkListItems[0])
    }

    @Test
    fun test_addDrink() = mainCoroutineRule.runBlockingTest {
        assertNull(getValue(viewModel.drinkAddedToCart))
        viewModel.addDrink(drinkListItems[0])
        assertTrue(getValue(viewModel.drinkAddedToCart))
        verify(addDrinkToCartUseCase, times(1)).invoke(drinkListItems[0])
        advanceUntilIdle()
        assertFalse(getValue(viewModel.drinkAddedToCart))
    }

    @Test
    fun test_double_addDrink() = mainCoroutineRule.runBlockingTest {
        viewModel.drinkAddedToCart.observeForever(drinkAddedToCartObserver)
        viewModel.addDrink(drinkListItems[0])
        verify(addDrinkToCartUseCase, times(1)).invoke(drinkListItems[0])
        advanceTimeBy(4_000)
        viewModel.addDrink(drinkListItems[1])
        verify(addDrinkToCartUseCase, times(1)).invoke(drinkListItems[1])
        advanceUntilIdle()
        verify(drinkAddedToCartObserver, times(2)).onChanged(drinkAddedToCartCaptor.capture())
        assertEquals(listOf(true, false), drinkAddedToCartCaptor.allValues)
    }
}