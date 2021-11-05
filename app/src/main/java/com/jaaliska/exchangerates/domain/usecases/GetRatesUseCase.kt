package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.ExchangeRates

interface GetRatesUseCase {

    suspend operator fun invoke(
        baseCurrencyCode: String,
        currencyCodes: List<String>? = null
    ): ExchangeRates
}