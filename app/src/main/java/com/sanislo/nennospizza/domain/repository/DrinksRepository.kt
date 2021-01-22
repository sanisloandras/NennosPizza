package com.sanislo.nennospizza.domain.repository

import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.api.data.ResourceResponse

interface DrinksRepository {
    suspend fun drinks(): ResourceResponse
}

class DrinksRepositoryImpl (private val dataService: DataService) :
    DrinksRepository {
    private var cache: ResourceResponse? = null

    override suspend fun drinks(): ResourceResponse {
        cache?.let { return it }
        val drinks = dataService.drinks()
        cache = drinks
        return drinks
    }
}