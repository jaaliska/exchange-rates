package com.jaaliska.exchangerates.data.mapper

import com.jaaliska.exchangerates.data.model.api.latestRates.RatesDetailsDto
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.domain.model.ExchangeRates

class ExchangeRatesMapper {

    fun map(
        rates: RatesDetailsDto,
        supportedCurrencies: Map<String, Currency>
    ): ExchangeRates {
        return ExchangeRates(
            date = rates.date,
            baseCurrencyCode = Currency(
                name = supportedCurrencies[rates.baseCurrencyCode]?.name ?: "",
                code = rates.baseCurrencyCode
            ),
            rates = rates.rates
                .filter { entry -> entry.key != rates.baseCurrencyCode }
                .map { entry ->
                    Rate(
                        currencyName = supportedCurrencies[entry.key]?.name ?: "",
                        currencyCode = entry.key,
                        exchangeRate = entry.value
                    )
                }
        )
    }
}