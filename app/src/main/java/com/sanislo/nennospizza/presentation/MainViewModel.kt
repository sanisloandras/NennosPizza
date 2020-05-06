package com.sanislo.nennospizza.presentation

import androidx.lifecycle.*
import com.sanislo.nennospizza.domain.usecase.*
import com.sanislo.nennospizza.presentation.cart.data.BaseCartItem
import com.sanislo.nennospizza.presentation.cart.data.Cart
import com.sanislo.nennospizza.presentation.drinks.DrinkListItem
import com.sanislo.nennospizza.presentation.list.PizzaListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val getPizzaListUseCase: GetPizzaListUseCase,
                    private val getPizzaDetailsUseCase: GetPizzaDetailsUseCase,
                    private val removeFromFromCartUseCase: RemoveFromCartUseCase,
                    private val checkoutUseCase: CheckoutUseCase,
                    private val getDrinksUseCase: GetDrinksUseCase,
                    private val addDrinkToCartUseCase: AddDrinkToCartUseCase,
                    cartUseCase: CartUseCase
) : ViewModel() {
    val pizzaList = MutableLiveData<List<PizzaListItem>>()

    private val _navigateToPizzaDetailsEvent = MutableLiveData<Event<String>>()
    val navigateToPizzaDetailsEvent: LiveData<Event<String>> = _navigateToPizzaDetailsEvent

    private val _navigateToCartEvent = MutableLiveData<Event<Unit>>()
    val navigateToCartEvent: LiveData<Event<Unit>> = _navigateToCartEvent

    private val _navigateToDrinksEvent = MutableLiveData<Event<Unit>>()
    val navigateToDrinksEvent: LiveData<Event<Unit>> = _navigateToDrinksEvent

    val cart: LiveData<Cart> = cartUseCase.invoke().asLiveData(Dispatchers.IO)

    private val _drinks = MutableLiveData<List<DrinkListItem>>()
    val drinks: LiveData<List<DrinkListItem>> = _drinks

    private val _drinkAddedToCart = MutableLiveData<Boolean>()
    val drinkAddedToCart: LiveData<Boolean> = _drinkAddedToCart

    private var drinkAddedToCartJob: Job? = null

    private val _errors = MutableLiveData<Event<Exception>>()
    val errors: LiveData<Event<Exception>> = _errors

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                pizzaList.postValue(getPizzaListUseCase.invoke())
            } catch (e: Exception) {
                _errors.postValue(Event(e))
            }
        }

    }

    fun onPizzaClick(pizzaListItem: PizzaListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _navigateToPizzaDetailsEvent.postValue(Event(pizzaListItem.name))
            } catch (e: Exception) {
                _errors.postValue(Event(e))
            }
        }
    }

    fun onRemoveCartItem(baseCartItem: BaseCartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            removeFromFromCartUseCase.invoke(baseCartItem)
        }
    }

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

    fun loadDrinks() {
        viewModelScope.launch(Dispatchers.IO) {
            _drinks.postValue(getDrinksUseCase.invoke())
        }
    }

    fun addDrink(drinkListItem: DrinkListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addDrinkToCartUseCase.invoke(drinkListItem)
            drinkAddedToCartJob?.cancel()
            drinkAddedToCartJob = drinkAddedToCartDelay()
        }
    }

    private suspend fun drinkAddedToCartDelay(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            _drinkAddedToCart.postValue(true)
            delay(ADD_TO_CART_DELAY)
            _drinkAddedToCart.postValue(false)
        }
    }

    fun onTapCart() {
        _navigateToCartEvent.value = Event(Unit)
    }

    fun onTapDrinks() {
        _navigateToDrinksEvent.value = Event(Unit)
    }

    companion object {
        val TAG = MainViewModel::class.java.simpleName
        const val ADD_TO_CART_DELAY = 5_000L
    }
}