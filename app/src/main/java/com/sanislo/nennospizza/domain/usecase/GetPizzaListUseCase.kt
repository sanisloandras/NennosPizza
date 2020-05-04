package com.sanislo.nennospizza.domain.usecase

import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository
import com.sanislo.nennospizza.presentation.list.PizzaListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GetPizzaListUseCase(private val pizzaRepository: PizzaRepository,
                          private val ingredientsRepository: IngredientsRepository
) {
    suspend fun invoke(): List<PizzaListItem> {
        return withContext(Dispatchers.IO) {
            val pizzasDeferred = async(Dispatchers.IO) { pizzaRepository.pizzas() }
            val ingredientsDeferred = async(Dispatchers.IO) { ingredientsRepository.ingredients() }
            val pizzas = pizzasDeferred.await()
            val ingredients = ingredientsDeferred.await()
            return@withContext pizzas.pizzas.map { pizza ->
                val pizzasIngredients = ingredients.filter { pizza.ingredients.contains(it.id) }
                val ingredientsString = pizzasIngredients.joinToString { it.name }
                val price = pizzas.basePrice + pizzasIngredients.map {
                    it.price
                }.reduce { a, b ->
                    a + b
                }
                PizzaListItem(
                    pizza.name,
                    ingredientsString,
                    "$$price",
                    pizza.imageUrl,
                    pizza.ingredients.toSet()
                )
            }
        }
    }

    //this can be used too
    /*suspend fun asFlow(): Flow<List<PizzaListItem>> {
        val pizzasFlow = flow {
            emit(pizzaRepository.pizzas())
        }.flowOn(Dispatchers.IO)
        val ingredientsFlow = flow {
            emit(ingredientsRepository.ingredients())
        }.flowOn(Dispatchers.IO)
        return pizzasFlow.zip(ingredientsFlow) { pizzas, ingredients ->
            pizzas.pizzas.map { pizza ->
                val pizzasIngredients = ingredients.filter { pizza.ingredients.contains(it.id) }
                val ingredientsString = pizzasIngredients.joinToString { it.name }
                val price = pizzas.basePrice + pizzasIngredients.map {
                    it.price
                }.reduce { a, b ->
                    a + b
                }
                PizzaListItem(
                    pizza.name,
                    ingredientsString,
                    "$$price",
                    pizza.imageUrl,
                    pizza.ingredients
                )
            }
        }
    }*/
}