package com.jaaliska.exchangerates.data.datasource

import com.jaaliska.exchangerates.data.currency.repository.RetrofitCurrencyRepository
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource

class CurrenciesDataSourceImpl(
    private val remoteCurrencyRepository: RetrofitCurrencyRepository,
    private val localCurrencyRepository: RoomCurrencyRepository,
) : CurrenciesDataSource {

    override suspend fun getFavorite(): List<Currency> {
        return localCurrencyRepository.readFavoriteCurrencies()
    }

    override suspend fun getSupported(forceUpdate: Boolean): List<Currency> {
        var currencies: List<Currency> = listOf()
        if (!forceUpdate) {
            currencies = localCurrencyRepository.readSupportedCurrencies()
        }
        if (forceUpdate || currencies.isEmpty()) {
            currencies = remoteCurrencyRepository.readSupportedCurrencies()
            localCurrencyRepository.saveSupportedCurrencies(currencies)
        }
        return currencies
    }

}