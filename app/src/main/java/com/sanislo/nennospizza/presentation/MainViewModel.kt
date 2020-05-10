package com.sanislo.nennospizza.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanislo.nennospizza.domain.usecase.GetPizzaListUseCase
import com.sanislo.nennospizza.domain.usecase.GetTransitionNameUseCase
import com.sanislo.nennospizza.presentation.details.PizzaDetailsInput
import com.sanislo.nennospizza.presentation.list.PizzaListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val getPizzaListUseCase: GetPizzaListUseCase,
                    private val getTransitionNameUseCase: GetTransitionNameUseCase
) : ViewModel() {
    val pizzaList = MutableLiveData<List<PizzaListItem>>()

    private val _navigateToPizzaDetailsEvent = MutableLiveData<Event<Pair<Int, PizzaDetailsInput>>>()
    val navigateToPizzaDetailsEvent: LiveData<Event<Pair<Int, PizzaDetailsInput>>> = _navigateToPizzaDetailsEvent

    private val _navigateToCartEvent = MutableLiveData<Event<Unit>>()
    val navigateToCartEvent: LiveData<Event<Unit>> = _navigateToCartEvent

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

    fun onPizzaClick(pizzaListItem: PizzaListItem, adapterPosition: Int) {
        val transitionName = getTransitionNameUseCase.invoke(adapterPosition)
        val pizzaDetailsInput = PizzaDetailsInput(pizzaListItem.name, pizzaListItem.imgUrl, transitionName)
        _navigateToPizzaDetailsEvent.value = Event(adapterPosition to pizzaDetailsInput)
    }

    fun onTapCart() {
        _navigateToCartEvent.value = Event(Unit)
    }

    companion object {
        val TAG = MainViewModel::class.java.simpleName
        const val ADD_TO_CART_DELAY = 5_000L
    }
}