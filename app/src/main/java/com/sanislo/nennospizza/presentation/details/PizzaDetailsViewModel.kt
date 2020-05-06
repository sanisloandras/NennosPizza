package com.sanislo.nennospizza.presentation.details

import android.util.Log.d
import androidx.lifecycle.*
import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository
import com.sanislo.nennospizza.domain.usecase.AddPizzaToCartUseCase
import com.sanislo.nennospizza.domain.usecase.GetIngredientsUseCase
import com.sanislo.nennospizza.domain.usecase.GetPizzaDetailsByNameUseCase
import com.sanislo.nennospizza.domain.usecase.GetPizzaPriceUseCase
import com.sanislo.nennospizza.presentation.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PizzaDetailsViewModel(
        val pizzaRepository: PizzaRepository,
        val ingredientsRepository: IngredientsRepository,
        private val getPizzaPriceUseCase: GetPizzaPriceUseCase,
        private val getPizzaDetailsByNameUseCase: GetPizzaDetailsByNameUseCase,
        private val getIngredientsUseCase: GetIngredientsUseCase,
        private val addPizzaToCartUseCase: AddPizzaToCartUseCase) : ViewModel() {

    private val _pizzaName = MutableLiveData<String>()
    private val userIngredientSelection = MutableLiveData<Set<Int>?>()
    private val inputData = MediatorLiveData<Pair<String, Set<Int>?>>()
    private val inputDataDelayed = inputData.asFlow().onStart { delay(100) }.asLiveData(Dispatchers.IO)

    private val _pizzaDetailsModel: LiveData<PizzaDetailsModel> = inputDataDelayed.switchMap { (pizzaName, userIngSelection) ->
        liveData {
            emit(getPizzaDetailsModel(pizzaName, userIngSelection))
        }
    }

    val pizzaDetails = _pizzaDetailsModel.map {
        PizzaDetails(it.name, it.imgUrl)
    }

    val ingredientListState = _pizzaDetailsModel.map {
        IngredientListState(it.ingredientList, it.selection)
    }

    private val _addToCartEnabled = MutableLiveData(false)
    private val _isPizzaLoaded = _pizzaDetailsModel.map { true }

    private val _addToCartState = MediatorLiveData<AddToCartState>()
    val addToCartState: LiveData<AddToCartState> = _addToCartState

    init {
        inputData.addSource(_pizzaName) {
            inputData.value = it to userIngredientSelection.value
        }
        inputData.addSource(userIngredientSelection, Observer {
            val pizzaName = _pizzaName.value ?: return@Observer
            inputData.value = pizzaName to it
        })

        _addToCartState.value = AddToCartState()
        _addToCartState.addSource(_isPizzaLoaded) {
            _addToCartState.value = _addToCartState.value?.copy(isLoaded = true, isEnabled = true)
        }
        _addToCartState.addSource(_addToCartEnabled) {
            _addToCartState.value = _addToCartState.value?.copy(isEnabled = it)
        }
        //used to display initial price
        _addToCartState.addSource(_pizzaDetailsModel) {
            _addToCartState.value = _addToCartState.value?.copy(isLoaded = true, price = it.initialPrice)
        }
    }

    private suspend fun getPizzaDetailsModel(pizzaName: String, userIngredientSelection: Set<Int>?): PizzaDetailsModel {
        d(TAG, "getPizzaDetailsModel")
        val pizzas = pizzaRepository.pizzas()
        val pizza = pizzas.pizzas.first { it.name == pizzaName }
        val ingredients = ingredientsRepository.ingredients()
        val ingredientListItems = mutableListOf<IngredientListItem>()
        val ingredientSelection = userIngredientSelection ?: pizza.ingredients.toSet()
        var initialPrice = pizzas.basePrice
        //non-reactive but efficient
        ingredients.forEach {
            ingredientListItems.add(IngredientListItem(it.id, it.name, "${it.price}"))
            if (ingredientSelection.contains(it.id)) {
                initialPrice += it.price
            }
        }
        return PizzaDetailsModel(pizza.name, pizza.imageUrl, ingredientListItems.toList(), ingredientSelection, initialPrice)
    }

    data class PizzaDetailsModel(
            val name: String,
            val imgUrl: String?,
            val ingredientList: List<IngredientListItem>,
            val selection: Set<Int>,
            val initialPrice: Double
    )

    fun setPizzaName(pizzaName: String?) {
        this._pizzaName.value = pizzaName
    }

    fun restoreState(pizzaName: String?, userIngredientSelection: Set<Int>) {
        setPizzaName(pizzaName)
        this.userIngredientSelection.value = userIngredientSelection
    }

    fun onSelectionChanged(id: Int, isSelected: Boolean) {
        viewModelScope.launch {
            val ingredientPrice = ingredientsRepository.ingredients().first { it.id == id }.price
            val priceChange = if (isSelected) ingredientPrice else ingredientPrice * -1
            viewModelScope.launch(Dispatchers.Main) {
                val addToCartState = _addToCartState.value?.let {
                    it.copy(price = it.price + priceChange)
                }
                _addToCartState.value = addToCartState
            }
        }
    }

    fun addPizzaToCart(selection: Set<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            _addToCartEnabled.postValue(false)
            delay(MainViewModel.ADD_TO_CART_DELAY)
            _addToCartEnabled.postValue(true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            pizzaDetails.value?.let {
                addPizzaToCartUseCase.invoke(it, selection)
            }
        }
    }

    companion object {
        val TAG = PizzaDetailsViewModel::class.java.simpleName
    }
}