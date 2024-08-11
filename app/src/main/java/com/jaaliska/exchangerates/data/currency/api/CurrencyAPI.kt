package com.jaaliska.exchangerates.data.currency.api

import com.jaaliska.exchangerates.data.currency.datasource.CurrencyType
import com.jaaliska.exchangerates.data.currency.model.api.BodyResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("currencies")
    suspend fun getSupportedCurrencies(
        @Query("type") currencyType: String? = CurrencyType.FIAT.value
    ): BodyResponseDto
}