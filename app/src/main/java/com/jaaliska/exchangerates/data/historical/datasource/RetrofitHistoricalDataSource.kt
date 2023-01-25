package com.jaaliska.exchangerates.data.historical.datasource

import com.jaaliska.exchangerates.data.error.NetworkErrorHandler
import com.jaaliska.exchangerates.data.historical.api.HistoricalApi
import com.jaaliska.exchangerates.data.mapper.ExchangeRatesMapper
import com.jaaliska.exchangerates.data.rates.model.api.ResponseDto
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RetrofitHistoricalDataSource(
    private val api: HistoricalApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val networkErrorHandler = NetworkErrorHandler()
    private val exchangeRatesMapper = ExchangeRatesMapper()

    suspend fun getHistoryForCurrency(
        date: Date,
        baseCurrency: Currency,
        currenciesRatesFor: List<Currency>
    ): ExchangeRates {
        val history: ResponseDto.RatesDetailsDto
        try {
            val timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            history = withContext(dispatcher) {
                api.getHistoryForCurrency(
                    timeFormat.format(date),
                    baseCurrency.code,
                    currenciesRatesFor.map { it.code }).response
            }
        } catch (e: Exception) {
            throw networkErrorHandler.mapError(e)
        }
        return exchangeRatesMapper.map(history, baseCurrency, currenciesRatesFor)
    }
}