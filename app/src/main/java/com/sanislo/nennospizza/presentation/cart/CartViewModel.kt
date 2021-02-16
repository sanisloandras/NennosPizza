package com.sanislo.nennospizza.presentation.cart

import androidx.lifecycle.*
import com.sanislo.nennospizza.domain.usecase.CheckoutUseCase
import com.sanislo.nennospizza.domain.usecase.RemoveFromCartUseCase
import com.sanislo.nennospizza.domain.usecase.cart.CartUseCase
import com.sanislo.nennospizza.presentation.Event
import com.sanislo.nennospizza.presentation.cart.adapter.CartListItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class CartViewModel(
        ioDispatcher: CoroutineDispatcher,
        private val checkoutUseCase: CheckoutUseCase,
        private val removeFromFromCartUseCase: RemoveFromCartUseCase,
        cartUseCase: CartUseCase) : ViewModel() {

    val cart = cartUseCase.invoke().asLiveData(viewModelScope.coroutineContext + ioDispatcher)

    private val _navigateToDrinksEvent = MutableLiveData<Event<Unit>>()
    val navigateToDrinksEvent: LiveData<Event<Unit>> = _navigateToDrinksEvent

    private val _navigateToThankYouEvent = MutableLiveData<Event<Unit>>()
    val navigateToThankYouEvent: LiveData<Event<Unit>> = _navigateToThankYouEvent

    private val _errors = MutableLiveData<Event<Exception>>()
    val errors: LiveData<Event<Exception>> = _errors

    fun checkout() {
        viewModelScope.launch {
            try {
                //todo maybe show progress for this, but the design is unknown
                checkoutUseCase.invoke()
                _navigateToThankYouEvent.value = Event(Unit)
            } catch (e: Exception) {
                _errors.value = Event(e)
            }
        }
    }

    fun onRemoveCartItem(cartListItem: CartListItem) {
        viewModelScope.launch {
            removeFromFromCartUseCase.invoke(cartListItem)
        }
    }

    fun onTapDrinks() {
        _navigateToDrinksEvent.value = Event(Unit)
    }
}