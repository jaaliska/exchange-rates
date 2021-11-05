package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.presentation.model.NamedExchangeRates

interface GetNamedRatesUseCase {
    suspend operator fun invoke(baseCurrencyCode: String): NamedExchangeRates
}