package com.jaaliska.exchangerates.data.currency

import com.jaaliska.exchangerates.data.currency.api.RetrofitCurrencyRepository
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class CurrencyDataSource(
    private val remoteRepository: RetrofitCurrencyRepository,
    private val localRepository: RoomCurrencyRepository
) {
    fun observeAll(): Flow<List<Currency>> {
        return localRepository.readSupportedCurrencies().onEach {
            if (it.isEmpty()) { refresh() }
        }
    }

    suspend fun refresh() {
        val currencies = remoteRepository.readSupportedCurrencies()
        localRepository.saveSupportedCurrencies(currencies)
    }
}