package com.jaaliska.exchangerates.data.rates.datasource

import com.jaaliska.exchangerates.data.error.NetworkErrorHandler
import com.jaaliska.exchangerates.data.mapper.ExchangeRatesMapper
import com.jaaliska.exchangerates.data.rates.api.RatesAPI
import com.jaaliska.exchangerates.data.rates.model.api.ResponseDto
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitRatesDataSource(
    private val api: RatesAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val networkErrorHandler = NetworkErrorHandler()
    private val exchangeRatesMapper = ExchangeRatesMapper()

    suspend fun getRates(
        baseCurrency: Currency,
        currencyCodes: List<Currency>
    ): ExchangeRates {
        val latestRates: ResponseDto.RatesDetailsDto = withContext(dispatcher) {
            try {
                api.getLatestRates(
                    baseCurrency.code,
                    currencyCodes.map { it.code }.mapListValuesToString()
                ).response
            } catch (e: Exception) {
                throw networkErrorHandler.mapError(e)
            }
        }
        return exchangeRatesMapper.map(latestRates, baseCurrency, currencyCodes)
    }

    private fun List<String>.mapListValuesToString(): String {
        return this.joinToString(",")
    }
}



