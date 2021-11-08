package com.jaaliska.exchangerates.data.currency.api

import retrofit2.http.GET

interface CurrencyAPI {

    @GET("currencies")
    suspend fun getSupportedCurrencies(): SupportedCurrenciesResponse

}