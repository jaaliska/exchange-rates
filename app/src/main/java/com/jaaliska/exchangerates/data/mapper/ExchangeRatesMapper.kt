package com.jaaliska.exchangerates.data.mapper

import com.jaaliska.exchangerates.data.model.latestRates.RatesDetailsDto
import com.jaaliska.exchangerates.domain.model.CurrencyDetails
import com.jaaliska.exchangerates.domain.model.CurrencyExchangeRate
import com.jaaliska.exchangerates.domain.model.ExchangeRates

class ExchangeRatesMapper {

    fun map(
        rates: RatesDetailsDto,
        supportedCurrencies: Map<String, CurrencyDetails>
    ): ExchangeRates {
        return ExchangeRates(
            date = rates.date,
            baseCurrency = CurrencyDetails(
                name = supportedCurrencies[rates.baseCurrency]?.name ?: "",
                code = rates.baseCurrency
            ),
            rates = rates.rates
                .filter { entry -> entry.key != rates.baseCurrency }
                .map { entry ->
                    CurrencyExchangeRate(
                        currencyName = supportedCurrencies[entry.key]?.name ?: "",
                        currencyCode = entry.key,
                        exchangeRate = entry.value
                    )
                }
        )
    }
}