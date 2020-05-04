package com.sanislo.nennospizza.presentation.details

import androidx.lifecycle.*
import com.sanislo.nennospizza.domain.usecase.AddPizzaToCartUseCase
import com.sanislo.nennospizza.domain.usecase.GetPizzaDetailsByNameUseCase
import com.sanislo.nennospizza.domain.usecase.GetPizzaPriceUseCase
import com.sanislo.nennospizza.presentation.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PizzaDetailsViewModel(private val getPizzaPriceUseCase: GetPizzaPriceUseCase,
                            private val getPizzaDetailsByNameUseCase: GetPizzaDetailsByNameUseCase,
                            private val addPizzaToCartUseCase: AddPizzaToCartUseCase): ViewModel() {
    private val _pizzaName = MutableLiveData<String>()

    private val _pizzaDetails: LiveData<PizzaDetails> = _pizzaName.switchMap {
        liveData<PizzaDetails>(Dispatchers.IO) {
            emit(getPizzaDetailsByNameUseCase.invoke(it))
        }
    }
    val pizzaDetails: LiveData<PizzaDetails> = _pizzaDetails

    private val _ingredientSelection = MediatorLiveData<Set<Int>>()

    private val _addToCartState = MediatorLiveData<AddToCartState>()
    val addToCartState: LiveData<AddToCartState> = _addToCartState

    private val _addToCartEnabled = MutableLiveData(false)

    private val _pizzaPrice: LiveData<Double> = _ingredientSelection.switchMap {
        liveData(Dispatchers.IO) {
            emit(getPizzaPriceUseCase.invoke(it))
        }
    }

    init {
        _addToCartState.value = AddToCartState()
        _addToCartState.addSource(_addToCartEnabled) {
            _addToCartState.value = _addToCartState.value?.copy(isEnabled = it)
        }
        _addToCartState.addSource(_pizzaPrice) {
            _addToCartState.value = _addToCartState.value?.copy(price = it)
        }
        _ingredientSelection.addSource(_pizzaDetails) {
            if (_ingredientSelection.value != null) return@addSource
            _ingredientSelection.value = it.initialSelection
        }
    }

    fun setPizzaName(pizzaName: String?) {
        this._pizzaName.value = pizzaName
    }

    fun onSelectionChanged(selection: Set<Int>) {
        _ingredientSelection.value = selection
    }

    fun addPizzaToCart() {
        viewModelScope.launch(Dispatchers.IO) {
            _addToCartEnabled.postValue(false)
            delay(MainViewModel.ADD_TO_CART_DELAY)
            _addToCartEnabled.postValue(true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            _pizzaDetails.value?.let {
                addPizzaToCartUseCase.invoke(it, _ingredientSelection.value ?: emptySet())
            }
        }
    }
}