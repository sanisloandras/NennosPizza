package com.sanislo.nennospizza.domain.repository

import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.api.data.IngOrDrinkResponse

interface IngredientsRepository {
    suspend fun ingredients(): IngOrDrinkResponse
}

class IngredientsRepositoryImpl(private val dataService: DataService) :
    IngredientsRepository {
    private var cache: IngOrDrinkResponse? = null

    override suspend fun ingredients(): IngOrDrinkResponse {
        cache?.let { return it }
        val ingredientsResponse = dataService.ingredients()
        cache = ingredientsResponse
        return ingredientsResponse
    }
}