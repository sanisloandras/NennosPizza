package com.sanislo.nennospizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.sanislo.nennospizza.domain.usecase.CheckoutUseCase
import com.sanislo.nennospizza.domain.usecase.RemoveFromCartUseCase
import com.sanislo.nennospizza.domain.usecase.cart.CartUseCase
import com.sanislo.nennospizza.presentation.cart.Cart
import com.sanislo.nennospizza.presentation.cart.CartViewModel
import com.sanislo.nennospizza.presentation.cart.adapter.CartListItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CartViewModelTest {
    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var cartUseCase: CartUseCase

    @Mock
    private lateinit var removeFromCartUseCase: RemoveFromCartUseCase

    @Mock
    private lateinit var checkoutUseCase: CheckoutUseCase

    @Mock
    private lateinit var mockObserver: Observer<Cart>

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<Cart>

    private val cartListItems = listOf(
            CartListItem("1", "some pizza", "$8.0"),
            CartListItem("2", "some drink", "$1.0"),
    )
    private val cart = Cart(cartListItems, 9.0)

    private lateinit var viewModel: CartViewModel

    @Before
    fun setupViewModel() {
        Mockito.`when`(cartUseCase.invoke()).thenReturn(flowOf(cart))
        viewModel = CartViewModel(mainCoroutineRule.dispatcher,
                checkoutUseCase,
                removeFromCartUseCase,
                cartUseCase)
    }

    @Test
    fun test_cartObserve() = mainCoroutineRule.runBlockingTest {
        viewModel.cart.observeForever(mockObserver)
        verify(mockObserver, times(1)).onChanged(argumentCaptor.capture())
        assertEquals(cart, argumentCaptor.value)
    }

    @Test
    fun test_onTapDrinks_navigateToDrinks() = mainCoroutineRule.runBlockingTest {
        viewModel.onTapDrinks()
        val actual = getValue(viewModel.navigateToDrinksEvent).getContentIfNotHandled()
        assertEquals(Unit, actual)
    }

    @Test
    fun test_onTapRemoveCartItem() = mainCoroutineRule.runBlockingTest {
        viewModel.onRemoveCartItem(cartListItems[0])
        verify(removeFromCartUseCase, times(1)).invoke(cartListItems[0])
    }

    @Test
    fun test_onTapCheckout() = mainCoroutineRule.runBlockingTest {
        viewModel.checkout()
        verify(checkoutUseCase, times(1)).invoke()
        assertEquals(Unit, getValue(viewModel.navigateToThankYouEvent).getContentIfNotHandled())
    }
}