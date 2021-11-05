package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecases.GetNamedRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetRatesUseCase
import com.jaaliska.exchangerates.presentation.model.NamedExchangeRates

class GetNamedRatesUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val getRatesUseCase: GetRatesUseCase,
) : GetNamedRatesUseCase {

    override suspend operator fun invoke(baseCurrencyCode: String): NamedExchangeRates {
        val favorites = localCurrencyRepository.readFavoriteCurrencies()
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
            baseCurrency = getCurrency(rates.baseCurrencyCode),
            rates = rates.rates.map {
                Pair(
                    getCurrency(it.currencyCode),
                    it.exchangeRate
                )
            }
        )
    }
}