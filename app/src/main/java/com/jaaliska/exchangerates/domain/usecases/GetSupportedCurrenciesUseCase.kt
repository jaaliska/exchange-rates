package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface GetSupportedCurrenciesUseCase {
    operator fun invoke(): Flow<List<Currency>>
}