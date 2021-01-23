package com.sanislo.nennospizza.presentation.details

import androidx.lifecycle.*
import com.sanislo.nennospizza.domain.usecase.AddPizzaToCartUseCase
import com.sanislo.nennospizza.domain.usecase.GetPizzaDetailsUseCase
import com.sanislo.nennospizza.domain.usecase.GetPizzaPriceChangeUseCase
import com.sanislo.nennospizza.presentation.details.list.IngredientItem
import com.sanislo.nennospizza.presentation.details.list.PizzaDetailsHeader
import com.sanislo.nennospizza.presentation.list.PizzaListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PizzaDetailsViewModel(
        private val getPizzaDetailsUseCase: GetPizzaDetailsUseCase,
        private val getPizzaPriceChangeUseCase: GetPizzaPriceChangeUseCase,
        private val addPizzaToCartUseCase: AddPizzaToCartUseCase
) : ViewModel() {

    val pizzaDetailsInput = MutableLiveData<PizzaDetailsInput>()
    private val userIngredientSelection = MutableLiveData<Set<Int>?>()

    private val inputData = MediatorLiveData<Pair<PizzaDetailsInput, Set<Int>?>>()
    private val inputDataDelayed = inputData.asFlow().onStart { delay(100) }.asLiveData(Dispatchers.IO)

    val pizzaName = pizzaDetailsInput.map { it.pizzaName }

    val pizzaDetailsState: LiveData<PizzaDetailsState> = inputDataDelayed.switchMap { (pizzaDetailsInput, userIngSelection) ->
        liveData {
            emit(getPizzaDetailsUseCase.invoke(pizzaDetailsInput, userIngSelection))
        }
    }

    private val _addToCartEnabled = MutableLiveData(false)
    private val _isPizzaLoaded = pizzaDetailsState.map { true }

    private val _addToCartState = MediatorLiveData<AddToCartState>()
    val addToCartState: LiveData<AddToCartState> = _addToCartState

    data class PizzaDetailsState(
            val header: PizzaDetailsHeader,
            val ingredients: List<IngredientItem>,
            val selection: Set<Int>,
            //todo does not belong here
            val initialPrice: Double
    )

    init {
        inputData.addSource(pizzaDetailsInput) {
            inputData.value = it to userIngredientSelection.value
        }
        inputData.addSource(userIngredientSelection, Observer {
            val pizzaDetailsInput = pizzaDetailsInput.value ?: return@Observer
            inputData.value = pizzaDetailsInput to it
        })

        _addToCartState.value = AddToCartState()
        _addToCartState.addSource(_isPizzaLoaded) {
            _addToCartState.value = _addToCartState.value?.copy(isLoaded = true, isEnabled = true)
        }
        _addToCartState.addSource(_addToCartEnabled) {
            _addToCartState.value = _addToCartState.value?.copy(isEnabled = it)
        }
        //used to display initial price
        _addToCartState.addSource(pizzaDetailsState) {
            _addToCartState.value = _addToCartState.value?.copy(isLoaded = true, price = it.initialPrice)
        }
    }

    fun restoreState(pizzaDetailsInput: PizzaDetailsInput?, userIngredientSelection: Set<Int>?) {
        this.pizzaDetailsInput.value = pizzaDetailsInput
        this.userIngredientSelection.value = userIngredientSelection
    }

    fun onSelectionChanged(id: Int, isSelected: Boolean) {
        viewModelScope.launch {
            val priceChange = getPizzaPriceChangeUseCase.invoke(id, isSelected)
            viewModelScope.launch(Dispatchers.Main) {
                val addToCartState = _addToCartState.value?.let { it.copy(price = it.price + priceChange) }
                _addToCartState.value = addToCartState
            }
        }
    }

    fun addPizzaToCart(selection: Set<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            _addToCartEnabled.postValue(false)
            delay(PizzaListViewModel.ADD_TO_CART_DELAY)
            _addToCartEnabled.postValue(true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            addPizzaToCartUseCase.invoke(pizzaName.value!!, addToCartState.value!!, selection)
        }
    }
}