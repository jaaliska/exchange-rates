package com.jaaliska.exchangerates.data.currency

import com.jaaliska.exchangerates.data.currency.api.RetrofitCurrencyRepository
import com.jaaliska.exchangerates.data.currency.persistence.CurrencyDao
import com.jaaliska.exchangerates.data.currency.persistence.RoomCurrency
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class MediatorCurrenciesDataSource(
    private val remoteRepository: RetrofitCurrencyRepository,
    private val dao: CurrencyDao
) : CurrenciesDataSource {

    override fun observeAll() =
        dao.readAll().map { roomCurrencies -> roomCurrencies.map(RoomCurrency::toDomain) }
            .onEach { if (it.isEmpty()) refresh() }

    override fun observeFavorites() = dao.readFavorites()
        .map { roomCurrencies -> roomCurrencies.map(RoomCurrency::toDomain) }

    suspend fun refresh() {
        val currencies = remoteRepository.readSupportedCurrencies()

        val list = currencies.map { currency -> RoomCurrency(currency) }
        dao.insert(list)
    }
}