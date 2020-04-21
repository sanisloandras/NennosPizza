package com.sanislo.nennospizza.domain.repository

import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.api.data.IngOrDrinkResponse

interface DrinksRepository {
    suspend fun drinks(): IngOrDrinkResponse
}

class DrinksRepositoryImpl(private val dataService: DataService) :
    DrinksRepository {
    private var cache: IngOrDrinkResponse? = null

    override suspend fun drinks(): IngOrDrinkResponse {
        cache?.let { return it }
        val drinks = dataService.drinks()
        cache = drinks
        return drinks
    }
}