package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates

interface GetRatesUseCase {

    suspend operator fun invoke(
        baseCurrencyCode: String,
        currencies: List<Currency>? = null
    ): ExchangeRates
}