package com.sanislo.nennospizza.presentation.drinks

import androidx.lifecycle.*
import com.sanislo.nennospizza.domain.ADD_TO_CART_DELAY
import com.sanislo.nennospizza.domain.usecase.AddDrinkToCartUseCase
import com.sanislo.nennospizza.domain.usecase.GetDrinksUseCase
import kotlinx.coroutines.*

class DrinkListViewModel(
        private val ioDispatcher: CoroutineDispatcher,
        private val getDrinksUseCase: GetDrinksUseCase,
        private val addDrinkToCartUseCase: AddDrinkToCartUseCase
) : ViewModel() {
    val drinks = liveData { emit(getDrinksUseCase.invoke()) }

    private val _drinkAddedToCart = MutableLiveData<Boolean>()
    val drinkAddedToCart: LiveData<Boolean> = _drinkAddedToCart.distinctUntilChanged()

    private var drinkAddedToCartJob: Job? = null

    private suspend fun drinkAddedToCartDelay(): Job {
        /*return viewModelScope.launch {
            _drinkAddedToCart.postValue(true)
            delay(ADD_TO_CART_DELAY)
            _drinkAddedToCart.postValue(false)
        }*/
        return viewModelScope.launch {
            _drinkAddedToCart.value = true
            delay(ADD_TO_CART_DELAY)
            _drinkAddedToCart.value = false
        }
    }

    fun addDrink(drinkListItem: DrinkListItem) {
        viewModelScope.launch {
            addDrinkToCartUseCase.invoke(drinkListItem)
            drinkAddedToCartJob?.cancel()
            drinkAddedToCartJob = drinkAddedToCartDelay()
        }
    }
}