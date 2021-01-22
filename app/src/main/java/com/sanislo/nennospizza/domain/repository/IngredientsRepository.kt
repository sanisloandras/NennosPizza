package com.sanislo.nennospizza.domain.repository

import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.api.data.ResourceResponse

interface IngredientsRepository {
    suspend fun ingredients(): ResourceResponse
}

class IngredientsRepositoryImpl(private val dataService: DataService) :
    IngredientsRepository {
    private var cache: ResourceResponse? = null

    override suspend fun ingredients(): ResourceResponse {
        cache?.let { return it }
        val ingredientsResponse = dataService.ingredients()
        cache = ingredientsResponse
        return ingredientsResponse
    }
}