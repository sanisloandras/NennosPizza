package com.sanislo.nennospizza.presentation.cart

import androidx.lifecycle.*
import com.sanislo.nennospizza.domain.usecase.CartUseCase
import com.sanislo.nennospizza.domain.usecase.CheckoutUseCase
import com.sanislo.nennospizza.domain.usecase.RemoveFromCartUseCase
import com.sanislo.nennospizza.presentation.Event
import com.sanislo.nennospizza.presentation.cart.data.BaseCartItem
import com.sanislo.nennospizza.presentation.cart.data.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(
        private val checkoutUseCase: CheckoutUseCase,
        private val removeFromFromCartUseCase: RemoveFromCartUseCase,
        cartUseCase: CartUseCase) : ViewModel() {

    val cart: LiveData<Cart> = cartUseCase.invoke().asLiveData(Dispatchers.IO)

    private val _navigateToDrinksEvent = MutableLiveData<Event<Unit>>()
    val navigateToDrinksEvent: LiveData<Event<Unit>> = _navigateToDrinksEvent

    private val _errors = MutableLiveData<Event<Exception>>()
    val errors: LiveData<Event<Exception>> = _errors

    fun checkout() {
        cart.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    checkoutUseCase.invoke(it)
                } catch (e: Exception) {
                    _errors.postValue(Event(e))
                }
            }
        }
    }

    fun onRemoveCartItem(baseCartItem: BaseCartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            removeFromFromCartUseCase.invoke(baseCartItem)
        }
    }

    fun onTapDrinks() {
        _navigateToDrinksEvent.value = Event(Unit)
    }
}