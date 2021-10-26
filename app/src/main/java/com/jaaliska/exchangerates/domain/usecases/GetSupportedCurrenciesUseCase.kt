package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import com.jaaliska.exchangerates.domain.repository.ReadonlyCurrencyRepository

class GetSupportedCurrenciesUseCase(
    private val remoteCurrenciesRepository: ReadonlyCurrencyRepository,
    private val localCurrenciesRepository: CurrencyRepository
) {
    suspend operator fun invoke(forceUpdate: Boolean): List<Currency> {
        var currencies: List<Currency> = listOf()
        if (!forceUpdate) {
            currencies = localCurrenciesRepository.readSupportedCurrencies()
        }
        if (forceUpdate || currencies.isEmpty()) {
            currencies = remoteCurrenciesRepository.readSupportedCurrencies()
            localCurrenciesRepository.saveSupportedCurrencies(currencies)
        }
        return currencies
    }
}