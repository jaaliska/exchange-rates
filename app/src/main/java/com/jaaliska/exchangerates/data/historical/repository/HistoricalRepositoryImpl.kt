package com.jaaliska.exchangerates.data.historical.repository

import com.jaaliska.exchangerates.data.historical.datasource.RetrofitHistoricalDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.repository.HistoricalRepository
import kotlinx.coroutines.*
import java.util.*

class HistoricalRepositoryImpl(
    private val retrofitHistoricalDataSource: RetrofitHistoricalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): HistoricalRepository {

    override suspend fun getYearHistoryForCurrency(
        year: Int,
        baseCurrency: Currency,
        currenciesRateFor: Currency
    ): List<ExchangeRates> {
        val currentYear = getCurrentYear()
        var month = 12
        if (year == currentYear) {
            month = getCurrentMonth()
        }
        val result: List<ExchangeRates>
        coroutineScope {
            val tasks = mutableListOf<Deferred<ExchangeRates>>()
            for (i in 0 until month) {
                tasks.add(
                    async(dispatcher) {
                        getRate(year, i, baseCurrency, currenciesRateFor)
                    })
            }
            result = awaitAll(*tasks.toTypedArray())
        }
        return result
    }

    private suspend fun getRate(
        year: Int,
        month: Int,
        baseCurrency: Currency,
        currenciesRateFor: Currency
    ): ExchangeRates {
        return retrofitHistoricalDataSource.getHistoryForCurrency(
            createDate(year, month, 1),
            baseCurrency,
            listOf(currenciesRateFor)
        )
    }

    private fun getCurrentYear(): Int {
        val c = Calendar.getInstance()
        return c.get(Calendar.YEAR)
    }

    private fun getCurrentMonth(): Int {
        val c = Calendar.getInstance()
        return c.get(Calendar.MONTH) + 1
    }

    private fun createDate(
        year: Int,
        month: Int,
        day: Int
    ): Date {
        val c = Calendar.getInstance()
        c.set(year, month, day)
        return c.time
    }
}