package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.FavoriteCurrenciesRepository
import com.jaaliska.exchangerates.domain.repository.ReadonlyCurrencyRepository
import com.jaaliska.exchangerates.presentation.model.NamedExchangeRates

class GetNamedRatesUseCase(
    private val localCurrencyRepository: ReadonlyCurrencyRepository,
    private val favoriteCurrencyRepository: FavoriteCurrenciesRepository,
    private val getRatesUseCase: GetRatesUseCase
) {
    suspend operator fun invoke(baseCurrencyCode: String): NamedExchangeRates {
        val favorites = favoriteCurrencyRepository.readFavoriteCurrencies()

        val codesToLoad = favorites.toMutableList()
        if (!codesToLoad.contains(baseCurrencyCode)) {
            codesToLoad.add(baseCurrencyCode)
        }

        val currencies =
            localCurrencyRepository.readSupportedCurrencies(codesToLoad).associateBy { it.code }
        val rates = getRatesUseCase(baseCurrencyCode, favorites)
        val getCurrency = { code: String -> currencies[code] ?: Currency("", code) }

        return NamedExchangeRates(
            date = rates.date,
            baseCurrency = getCurrency(baseCurrencyCode),
            rates = rates.rates.map {
                Pair(
                    getCurrency(it.currencyCode),
                    it.exchangeRate
                )
            }
        )
    }
}