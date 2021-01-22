package com.sanislo.nennospizza.domain.usecase

import android.util.Log.d
import com.sanislo.nennospizza.domain.repository.IngredientsRepository
import com.sanislo.nennospizza.domain.repository.PizzaRepository
import com.sanislo.nennospizza.presentation.list.PizzaListItem
import kotlinx.coroutines.*

class GetPizzaListUseCase(private val pizzaRepository: PizzaRepository,
                          private val ingredientsRepository: IngredientsRepository
) {
    suspend fun invoke(): List<PizzaListItem> {
        return withContext(Dispatchers.IO) {
            val pizzasDeferred = async { pizzaRepository.pizzas() }
            val ingredientsDeferred = async { ingredientsRepository.ingredients() }
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

    /*private suspend fun temp() {
        val start = System.currentTimeMillis()
        d(TAG, "temp start")
        val t1 = withContext(Dispatchers.IO) {
            delay(1000)
        }
        val t2 = withContext(Dispatchers.IO) {
            delay(2000)
        }
        d(TAG, "temp end ${System.currentTimeMillis() - start}")
    }

    private suspend fun temp2() {
        withContext(Dispatchers.IO) {
            val start = System.currentTimeMillis()
            d(TAG, "temp start")
            val t1 = async {
                delay(1000)
            }
            val t2 = async {
                delay(2000)
            }
            awaitAll(t1, t2)
            d(TAG, "temp end ${System.currentTimeMillis() - start}")
        }
    }*/

    companion object {
        private const val TAG = "PizzaListUseCase"
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