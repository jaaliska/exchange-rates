package com.jaaliska.exchangerates.data.rates.api

import com.jaaliska.exchangerates.data.rates.model.api.ResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesAPI {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") baseCurrency: String,
        @Body symbols: List<String> = listOf()
    ): ResponseDto.RatesDetailsDto
}