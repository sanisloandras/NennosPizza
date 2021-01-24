package com.sanislo.nennospizza.presentation.drinks

import android.util.Log.d
import androidx.lifecycle.*
import com.sanislo.nennospizza.domain.ADD_TO_CART_DELAY
import com.sanislo.nennospizza.domain.usecase.AddDrinkToCartUseCase
import com.sanislo.nennospizza.domain.usecase.GetDrinksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DrinkListViewModel(
        private val getDrinksUseCase: GetDrinksUseCase,
        private val addDrinkToCartUseCase: AddDrinkToCartUseCase
) : ViewModel() {
    val drinks = liveData(Dispatchers.IO) {
        emit(getDrinksUseCase.invoke())
    }

    private val _drinkAddedToCart = MutableLiveData<Boolean>()
    val drinkAddedToCart: LiveData<Boolean> = _drinkAddedToCart

    private var drinkAddedToCartJob: Job? = null

    private suspend fun drinkAddedToCartDelay(): Job {
        return viewModelScope.launch {
            _drinkAddedToCart.postValue(true)
            delay(ADD_TO_CART_DELAY)
            _drinkAddedToCart.postValue(false)
        }
    }

    fun addDrink(drinkListItem: DrinkListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addDrinkToCartUseCase.invoke(drinkListItem)
            drinkAddedToCartJob?.cancel()
            drinkAddedToCartJob = drinkAddedToCartDelay()
        }
    }
}