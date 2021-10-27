package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.presentation.model.NamedExchangeRates
import kotlinx.coroutines.flow.Flow

interface GetNamedRatesUseCase {
    suspend operator fun invoke(baseCurrencyCode: String): Flow<NamedExchangeRates>
}