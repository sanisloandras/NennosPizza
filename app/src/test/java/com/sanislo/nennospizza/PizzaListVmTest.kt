package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.sanislo.nennospizza.domain.usecase.GetPizzaListUseCase
import com.sanislo.nennospizza.domain.usecase.GetTransitionNameUseCase
import com.sanislo.nennospizza.presentation.Event
import com.sanislo.nennospizza.presentation.details.PizzaDetailsInput
import com.sanislo.nennospizza.presentation.list.PizzaListItem
import com.sanislo.nennospizza.presentation.list.PizzaListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PizzaListVmTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var pizzaListObserver: Observer<List<PizzaListItem>>

    @Captor
    private lateinit var pizzaListCaptor: ArgumentCaptor<List<PizzaListItem>>

    @Mock
    private lateinit var errorObserver: Observer<Event<Exception>>

    @Mock
    private lateinit var getPizzaDetailsUseCase: GetPizzaListUseCase

    @Mock
    private lateinit var getTransitionNameUseCase: GetTransitionNameUseCase

    private lateinit var viewModel: PizzaListViewModel

    private val pizzaList = listOf(
            PizzaListItem("p0", "i1, i2", "$1.0", "img0", setOf(1, 2)),
            PizzaListItem("p1", "i3, i4", "$2.0", "img1", setOf(2, 3)),
    )

    @Before
    fun setupViewModel() {
        `when`(getTransitionNameUseCase.invoke(anyInt())).thenReturn("mock_transition_name")
        viewModel = PizzaListViewModel(getPizzaDetailsUseCase,
                getTransitionNameUseCase)
    }

    @Test
    fun testSuccess() = mainCoroutineRule.runBlockingTest {
        `when`(getPizzaDetailsUseCase.invoke()).thenReturn(pizzaList)

        viewModel.errors.observeForever(errorObserver)
        viewModel.pizzaList.observeForever(pizzaListObserver)
        viewModel.load()
        advanceUntilIdle()

        verify(pizzaListObserver, times(1)).onChanged(pizzaListCaptor.capture())
        verifyNoInteractions(errorObserver)
        assertEquals(pizzaList, pizzaListCaptor.value)
    }

    @Test
    fun testFailure() = mainCoroutineRule.runBlockingTest {
        `when`(getPizzaDetailsUseCase.invoke()).thenThrow(Exception("Some error, like no internet connection..."))

        viewModel.errors.observeForever(errorObserver)
        viewModel.pizzaList.observeForever(pizzaListObserver)
        viewModel.load()
        advanceUntilIdle()

        verify(errorObserver, times(1)).onChanged(any())
        verifyNoInteractions(pizzaListObserver)
    }

    @Test
    fun testOnTapCart() {
        viewModel.onTapCart()
        assertNotNull(getValue(viewModel.navigateToCartEvent).getContentIfNotHandled())
    }

    @Test
    fun testOnPizzaClick() {
        viewModel.onPizzaClick(pizzaList[1], 1)
        val actual = getValue(viewModel.navigateToPizzaDetailsEvent).getContentIfNotHandled()
        val expected = 1 to PizzaDetailsInput("p1", "img1", "mock_transition_name")
        assertEquals(expected, actual)
    }
}