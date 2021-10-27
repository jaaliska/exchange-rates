package com.jaaliska.exchangerates.data.currency.api

import com.jaaliska.exchangerates.data.currency.model.api.BodyResponseDto
import retrofit2.http.GET

interface CurrencyAPI {

    @GET("currencies")
    suspend fun getSupportedCurrencies(): BodyResponseDto

}