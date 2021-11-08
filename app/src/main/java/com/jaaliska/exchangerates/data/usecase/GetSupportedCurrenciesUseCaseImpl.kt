package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.CurrencyDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import kotlinx.coroutines.flow.Flow

class GetSupportedCurrenciesUseCaseImpl(
    private val currencyDataSource: CurrencyDataSource
) : GetSupportedCurrenciesUseCase {

    override operator fun invoke(): Flow<List<Currency>> {
        return currencyDataSource.observeAll()
    }
}