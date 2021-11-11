package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.usecases.GetNamedRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetRatesUseCase

class GetNamedRatesUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val getRatesUseCase: GetRatesUseCase,
) : GetNamedRatesUseCase {

    override suspend operator fun invoke(baseCurrencyCode: String): ExchangeRates {
        val favorites = localCurrencyRepository.readFavoriteCurrencies()
        val codesToLoad = favorites.map { it.code }.toMutableList()
        if (!codesToLoad.contains(baseCurrencyCode)) {
            codesToLoad.add(baseCurrencyCode)
        }

        val currencies =
            localCurrencyRepository.readSupportedCurrencies(codesToLoad).associateBy { it.code }
        val rates = getRatesUseCase(baseCurrencyCode, favorites)
        val getCurrency = { code: String -> currencies[code] ?: Currency("", code) }

        return ExchangeRates(
            date = rates.date,
            baseCurrency = getCurrency(rates.baseCurrency.code),
            rates = rates.rates
        )
    }
}