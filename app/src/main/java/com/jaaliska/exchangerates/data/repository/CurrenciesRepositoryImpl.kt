package com.jaaliska.exchangerates.data.repository

import com.jaaliska.exchangerates.data.currency.datasource.RetrofitCurrencyDataSource
import com.jaaliska.exchangerates.data.currency.datasource.RoomCurrencyDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.CurrenciesRepository

class CurrenciesRepositoryImpl(
    private val remoteCurrencyDataSource: RetrofitCurrencyDataSource,
    private val localCurrencyDataSource: RoomCurrencyDataSource,
) : CurrenciesRepository {

    override suspend fun getFavorite(): List<Currency> {
        return localCurrencyDataSource.readFavoriteCurrencies()
    }

    override suspend fun getSupported(forceUpdate: Boolean): List<Currency> {
        var currencies: List<Currency> = listOf()
        if (!forceUpdate) {
            currencies = localCurrencyDataSource.readSupportedCurrencies()
        }
        if (forceUpdate || currencies.isEmpty()) {
            currencies = remoteCurrencyDataSource.readSupportedCurrencies()
            localCurrencyDataSource.saveSupportedCurrencies(currencies)
        }
        return currencies
    }

}