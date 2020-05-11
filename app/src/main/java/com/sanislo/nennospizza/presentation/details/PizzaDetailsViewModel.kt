package com.sanislo.nennospizza.presentation.details

import android.util.Log.d
import androidx.lifecycle.*
import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository
import com.sanislo.nennospizza.presentation.details.list.BasePizzaDetailsItem
import com.sanislo.nennospizza.presentation.details.list.IngredientItem
import com.sanislo.nennospizza.presentation.details.list.PizzaImageItem
import com.sanislo.nennospizza.presentation.list.PizzaListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PizzaDetailsViewModel(
        private val pizzaRepository: PizzaRepository,
        private val ingredientsRepository: IngredientsRepository) : ViewModel() {

    val pizzaDetailsInput = MutableLiveData<PizzaDetailsInput>()
    private val userIngredientSelection = MutableLiveData<Set<Int>?>()

    private val inputData = MediatorLiveData<Pair<PizzaDetailsInput, Set<Int>?>>()
    private val inputDataDelayed = inputData.asFlow().onStart { delay(100) }.asLiveData(Dispatchers.IO)

    val pizzaName = pizzaDetailsInput.map { it.pizzaName }

    val pizzaDetailsState: LiveData<PizzaDetailsState> = inputDataDelayed.switchMap { (pizzaDetailsInput, userIngSelection) ->
        liveData {
            emit(getPizzaDetailsState(pizzaDetailsInput, userIngSelection))
        }
    }

    private val _addToCartEnabled = MutableLiveData(false)
    private val _isPizzaLoaded = pizzaDetailsState.map { true }

    private val _addToCartState = MediatorLiveData<AddToCartState>()
    val addToCartState: LiveData<AddToCartState> = _addToCartState

    data class PizzaDetailsState(
            val list: List<BasePizzaDetailsItem>,
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

    private suspend fun getPizzaDetailsState(input: PizzaDetailsInput, userIngredientSelection: Set<Int>?): PizzaDetailsState {
        d(TAG, "getPizzaDetailsState")
        val pizzas = pizzaRepository.pizzas()
        val pizza = pizzas.pizzas.first { it.name == input.pizzaName }
        val ingredients = ingredientsRepository.ingredients()
        val ingredientItems = mutableListOf<IngredientItem>()
        val selection = userIngredientSelection ?: pizza.ingredients.toSet()
        var initialPrice = pizzas.basePrice
        //non-reactive but efficient
        ingredients.forEach {
            ingredientItems.add(IngredientItem(it.id, it.name, "${it.price}"))
            if (selection.contains(it.id)) {
                initialPrice += it.price
            }
        }
        val pizzaDetailsList = mutableListOf<BasePizzaDetailsItem>()
        pizzaDetailsList.add(PizzaImageItem(input.imgUrl, input.transitionName))
        pizzaDetailsList.addAll(ingredientItems)
        return PizzaDetailsState(pizzaDetailsList, selection, initialPrice)
    }

    fun restoreState(pizzaDetailsInput: PizzaDetailsInput?, userIngredientSelection: Set<Int>?) {
        this.pizzaDetailsInput.value = pizzaDetailsInput
        this.userIngredientSelection.value = userIngredientSelection
    }

    fun onSelectionChanged(id: Int, isSelected: Boolean) {
        viewModelScope.launch {
            val ingredientPrice = ingredientsRepository.ingredients().first { it.id == id }.price
            val priceChange = if (isSelected) ingredientPrice else ingredientPrice * -1
            viewModelScope.launch(Dispatchers.Main) {
                val addToCartState = _addToCartState.value?.let { it.copy(price = it.price + priceChange) }
                _addToCartState.value = addToCartState
            }
        }
    }

    fun addPizzaToCart() {
        viewModelScope.launch(Dispatchers.IO) {
            _addToCartEnabled.postValue(false)
            delay(PizzaListViewModel.ADD_TO_CART_DELAY)
            _addToCartEnabled.postValue(true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            //todo
        }
    }

    companion object {
        val TAG = PizzaDetailsViewModel::class.java.simpleName
    }
}