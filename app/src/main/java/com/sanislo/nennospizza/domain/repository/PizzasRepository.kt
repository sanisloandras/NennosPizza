package com.sanislo.nennospizza.domain.repository

import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.api.data.PizzasResponse

interface PizzaRepository {
    suspend fun pizzas(): PizzasResponse
}

class PizzaRepositoryImpl(private val dataService: DataService) : PizzaRepository {
    //the simplest cache ever
    private var cache: PizzasResponse? = null

    override suspend fun pizzas(): PizzasResponse {
        cache?.let { return it }
        val pizzasResponse = dataService.pizzas()
        cache = pizzasResponse
        return pizzasResponse
    }
}