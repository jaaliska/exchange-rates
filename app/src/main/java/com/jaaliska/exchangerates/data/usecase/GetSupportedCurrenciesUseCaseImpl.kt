package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RetrofitCurrencyRepository
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase

class GetSupportedCurrenciesUseCaseImpl(
    private val remoteCurrencyRepository: RetrofitCurrencyRepository,
    private val localCurrenciesRepository: RoomCurrencyRepository
) : GetSupportedCurrenciesUseCase {

    override suspend operator fun invoke(forceUpdate: Boolean): List<Currency> {
        var currencies: List<Currency> = listOf()
        if (!forceUpdate) {
            currencies = localCurrenciesRepository.readSupportedCurrencies()
        }
        if (forceUpdate || currencies.isEmpty()) {
            currencies = remoteCurrencyRepository.readSupportedCurrencies()
            localCurrenciesRepository.saveSupportedCurrencies(currencies)
        }
        return currencies
    }
}