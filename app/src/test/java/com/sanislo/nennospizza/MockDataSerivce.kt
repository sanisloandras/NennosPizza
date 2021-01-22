package com.sanislo.nennospizza

import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.api.data.ResourceResponse
import com.sanislo.nennospizza.api.data.PizzasResponse
import org.mockito.Mockito

class MockDataSerivce {
    companion object {
        suspend fun create(): DataService {
            val dataSerivce = Mockito.mock(DataService::class.java)
            val gson = Gson()
            whenever(dataSerivce.pizzas()).thenReturn(gson.fromJson(FakeDataService.PIZZAS, PizzasResponse::class.java))
            whenever(dataSerivce.ingredients()).thenReturn(gson.fromJson(FakeDataService.INGREDIENTS, ResourceResponse::class.java))
            whenever(dataSerivce.drinks()).thenReturn(gson.fromJson(FakeDataService.DRINKS, ResourceResponse::class.java))
            return dataSerivce
        }
    }
}