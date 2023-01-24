package com.jaaliska.exchangerates.data.historical.api

import com.jaaliska.exchangerates.data.rates.model.api.ResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoricalApi {

    @GET("historical")
    suspend fun getHistoryForCurrency(
        @Query("date") date: String,
        @Query("base") baseCurrency: String,
        @Query("symbols") symbols: List<String>
    ): ResponseDto
}