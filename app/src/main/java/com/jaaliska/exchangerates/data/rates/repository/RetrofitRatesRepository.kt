package com.jaaliska.exchangerates.data.rates.repository

import com.jaaliska.exchangerates.data.error.NetworkErrorHandler
import com.jaaliska.exchangerates.data.rates.api.RatesAPI
import com.jaaliska.exchangerates.data.rates.model.api.ResponseDto
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RetrofitRatesRepository(
    private val api: RatesAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val networkErrorHandler = NetworkErrorHandler()

    suspend fun getRates(
        baseCurrencyCode: String,
        currencyCodes: List<String>
    ): ExchangeRates {
        val latestRates: ResponseDto.RatesDetailsDto
        try {
            latestRates = api.getLatestRates(baseCurrencyCode, currencyCodes)
        } catch (e: Exception) {
            throw networkErrorHandler.mapError(e)
        }
        return ExchangeRates(
            date = latestRates.date,
            baseCurrencyCode = baseCurrencyCode,
            rates = latestRates.rates.map {
                Rate(
                    currencyCode = it.key,
                    exchangeRate = it.value,
                )
            }
        )
    }
}



