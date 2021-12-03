package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.SetFavoriteCurrenciesUseCase

class SetFavoriteCurrenciesUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val ratesDataSource: RatesDataSource,
    private val preferencesRepository: PreferencesRepository
) : SetFavoriteCurrenciesUseCase {

    @Throws(IllegalFavoritesCountException::class)
    override suspend fun invoke(codes: Set<String>) {
        if (codes.count() < 2) throw IllegalFavoritesCountException()
        localCurrencyRepository.saveFavoriteCurrencies(codes)
        val baseCurrencyCode = preferencesRepository.getBaseCurrencyCode()
        if (!codes.contains(baseCurrencyCode)) {
            preferencesRepository.setBaseCurrencyCode(codes.first())
        }
        ratesDataSource.refresh()
    }

}