package com.sanislo.nennospizza.api.checkout

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckoutService {
    @POST("post")
    suspend fun checkout(@Body checkout: Checkout): ResponseBody
}