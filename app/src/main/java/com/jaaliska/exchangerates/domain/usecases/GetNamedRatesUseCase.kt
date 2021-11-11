package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.ExchangeRates


interface GetNamedRatesUseCase {
    suspend operator fun invoke(baseCurrencyCode: String): ExchangeRates
}