package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.data.currency.datasource.RoomCurrencyDataSource
import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.repository.RatesRepository

class SetFavoriteCurrenciesUseCase(
    private val localCurrencyDataSource: RoomCurrencyDataSource,
    private val ratesRepository: RatesRepository,
    private val preferencesRepository: PreferencesRepository
)  {

    @Throws(IllegalFavoritesCountException::class)
    suspend operator fun invoke(codes: Set<String>) {
        if (codes.count() < 2) throw IllegalFavoritesCountException()
        localCurrencyDataSource.saveFavoriteCurrencies(codes)
        val baseCurrencyCode = preferencesRepository.getBaseCurrencyCode()
        if (!codes.contains(baseCurrencyCode)) {
            preferencesRepository.setBaseCurrencyCode(codes.first())
        }
        ratesRepository.refresh()
    }
}