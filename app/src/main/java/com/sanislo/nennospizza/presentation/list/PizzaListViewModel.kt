package com.sanislo.nennospizza.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanislo.nennospizza.domain.usecase.GetPizzaListUseCase
import com.sanislo.nennospizza.domain.usecase.GetTransitionNameUseCase
import com.sanislo.nennospizza.presentation.Event
import com.sanislo.nennospizza.presentation.details.PizzaDetailsInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PizzaListViewModel(private val getPizzaListUseCase: GetPizzaListUseCase,
                         private val getTransitionNameUseCase: GetTransitionNameUseCase
) : ViewModel() {
    private val _pizzaList = MutableLiveData<List<PizzaListItem>>()
    val pizzaList: LiveData<List<PizzaListItem>> = _pizzaList

    private val _navigateToPizzaDetailsEvent = MutableLiveData<Event<Pair<Int, PizzaDetailsInput>>>()
    val navigateToPizzaDetailsEvent: LiveData<Event<Pair<Int, PizzaDetailsInput>>> = _navigateToPizzaDetailsEvent

    private val _navigateToCartEvent = MutableLiveData<Event<Unit>>()
    val navigateToCartEvent: LiveData<Event<Unit>> = _navigateToCartEvent

    private val _errors = MutableLiveData<Event<Exception>>()
    val errors: LiveData<Event<Exception>> = _errors

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //todo mimic longer load time, should remove for production x)
                delay(1000)
                _pizzaList.postValue(getPizzaListUseCase.invoke())
            } catch (e: Exception) {
                _errors.postValue(Event(e))
            }
            _isLoading.postValue(false)
        }
    }

    fun onPizzaClick(pizzaListItem: PizzaListItem, adapterPosition: Int) {
        val transitionName = getTransitionNameUseCase.invoke(adapterPosition)
        val pizzaDetailsInput = PizzaDetailsInput(pizzaListItem.name, pizzaListItem.imgUrl, transitionName)
        _navigateToPizzaDetailsEvent.value = Event(adapterPosition to pizzaDetailsInput)
    }

    fun onTapCart() {
        _navigateToCartEvent.value = Event(Unit)
    }
}