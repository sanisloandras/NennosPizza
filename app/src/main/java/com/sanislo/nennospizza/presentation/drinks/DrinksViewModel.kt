package com.sanislo.nennospizza.presentation.drinks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanislo.nennospizza.domain.usecase.AddDrinkToCartUseCase
import com.sanislo.nennospizza.domain.usecase.GetDrinksUseCase
import com.sanislo.nennospizza.presentation.list.PizzaListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DrinksViewModel(
        private val getDrinksUseCase: GetDrinksUseCase,
        private val addDrinkToCartUseCase: AddDrinkToCartUseCase
) : ViewModel() {
    private val _drinks = MutableLiveData<List<DrinkListItem>>()
    val drinks: LiveData<List<DrinkListItem>> = _drinks

    private val _drinkAddedToCart = MutableLiveData<Boolean>()
    val drinkAddedToCart: LiveData<Boolean> = _drinkAddedToCart

    private var drinkAddedToCartJob: Job? = null

    init {
        loadDrinks()
    }

    private fun loadDrinks() {
        viewModelScope.launch(Dispatchers.IO) {
            _drinks.postValue(getDrinksUseCase.invoke())
        }
    }

    private suspend fun drinkAddedToCartDelay(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            _drinkAddedToCart.postValue(true)
            delay(PizzaListViewModel.ADD_TO_CART_DELAY)
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