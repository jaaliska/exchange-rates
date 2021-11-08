package com.jaaliska.exchangerates.data.rates.api

import retrofit2.http.GET
import retrofit2.http.Query

interface RatesAPI {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") baseCurrencyCode: String,
        @Query("symbols") symbols: String
    ): RatesResponse
}