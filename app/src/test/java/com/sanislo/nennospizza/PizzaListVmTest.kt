package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.sanislo.nennospizza.domain.usecase.GetPizzaListUseCase
import com.sanislo.nennospizza.domain.usecase.GetTransitionNameUseCase
import com.sanislo.nennospizza.presentation.Event
import com.sanislo.nennospizza.presentation.list.PizzaListItem
import com.sanislo.nennospizza.presentation.list.PizzaListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verifyNoInteractions
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

    private lateinit var viewModel: PizzaListViewModel

    @Before
    fun setupViewModel() {
        viewModel = PizzaListViewModel(mainCoroutineRule.dispatcher,
                getPizzaDetailsUseCase,
                Mockito.mock(GetTransitionNameUseCase::class.java))
    }

    @Test
    fun testSuccess() = mainCoroutineRule.runBlockingTest {
        val expected = listOf(
                PizzaListItem("p1", "ingredients", "$1.0", "img", setOf(1, 2)),
                PizzaListItem("p2", "ingredients2", "$2.0", "img2", setOf(2, 3)),
        )

        `when`(getPizzaDetailsUseCase.invoke()).thenReturn(expected)

        viewModel.errors.observeForever(errorObserver)
        viewModel.pizzaList.observeForever(pizzaListObserver)
        viewModel.load()
        advanceUntilIdle()

        verify(pizzaListObserver, times(1)).onChanged(pizzaListCaptor.capture())
        verifyNoInteractions(errorObserver)
        assertEquals(expected, pizzaListCaptor.value)
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
}