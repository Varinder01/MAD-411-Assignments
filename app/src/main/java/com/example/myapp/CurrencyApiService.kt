package com.example.myapp


import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {

    @GET("v1/currencies/{base}.json")
    suspend fun getExchangeRates(@Path("base") base: String): Response<JsonObject>
}
