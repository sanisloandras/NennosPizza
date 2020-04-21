package com.sanislo.nennospizza.api.data

import retrofit2.http.GET

interface DataService {
    @GET("ingredients.json")
    suspend fun ingredients(): IngOrDrinkResponse

    @GET("drinks.json")
    suspend fun drinks(): IngOrDrinkResponse

    @GET("pizzas.json")
    suspend fun pizzas(): PizzasResponse
}