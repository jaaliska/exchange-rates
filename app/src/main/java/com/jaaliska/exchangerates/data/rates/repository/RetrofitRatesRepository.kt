package com.jaaliska.exchangerates.data.rates.repository

import com.jaaliska.exchangerates.data.error.NetworkErrorHandler
import com.jaaliska.exchangerates.data.rates.api.RatesAPI
import com.jaaliska.exchangerates.data.rates.model.api.ResponseDto
import com.jaaliska.exchangerates.domain.CurrencyNotFoundException
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate

class RetrofitRatesRepository(
    private val api: RatesAPI
) {
    private val networkErrorHandler = NetworkErrorHandler()

    suspend fun getRates(
        baseCurrency: Currency,
        currencyCodes: List<Currency>
    ): ExchangeRates {
        val latestRates: ResponseDto.RatesDetailsDto
        try {
            latestRates =
                api.getLatestRates(
                    baseCurrency.code,
                    currencyCodes.map { it.code }.mapListValuesToString()
                ).response
        } catch (e: Exception) {
            throw networkErrorHandler.mapError(e)
        }
        return ExchangeRates(
            date = latestRates.date,
            baseCurrency = baseCurrency,
            rates = latestRates.rates.map {
                Rate(
                    currency = Currency(
                        code = it.key,
                        name = currencyCodes.find { currency ->
                            currency.code == it.key
                        }?.name ?: throw CurrencyNotFoundException(it.key)
                    ),
                    exchangeRate = it.value,
                )
            }
        )
    }

    private fun List<String>.mapListValuesToString(): String {
        return this.joinToString(",")
    }
}



