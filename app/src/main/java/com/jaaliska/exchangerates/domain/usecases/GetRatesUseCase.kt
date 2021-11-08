package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.ExchangeRates
import kotlinx.coroutines.flow.Flow

interface GetRatesUseCase {
    operator fun invoke(): Flow<ExchangeRates?>
}