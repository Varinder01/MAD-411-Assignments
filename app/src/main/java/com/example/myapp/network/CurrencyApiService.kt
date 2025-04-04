package com.example.myapp.network

import android.icu.util.Currency
import retrofit2.http.GET

interface CurrencyApiService {
    @GET("currencies/{currency}.json")
    suspend fun getCurrencies(): List<Currency>
}