package com.jaaliska.exchangerates.data.api

import com.jaaliska.exchangerates.data.model.latestRates.ResponseDto
import com.jaaliska.exchangerates.data.model.supportedCurrencies.BodyResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("currencies")
    suspend fun getSupportedCurrencies(): BodyResponseDto

    @GET("latest")
    suspend fun getLatestRates(@Query("base")baseCurrency : String): ResponseDto
}