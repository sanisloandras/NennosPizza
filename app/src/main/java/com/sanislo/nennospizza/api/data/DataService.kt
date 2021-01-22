package com.sanislo.nennospizza.api.data

import retrofit2.http.GET

interface DataService {
    @GET("ingredients.json")
    suspend fun ingredients(): ResourceResponse

    @GET("drinks.json")
    suspend fun drinks(): ResourceResponse

    @GET("pizzas.json")
    suspend fun pizzas(): PizzasResponse
}