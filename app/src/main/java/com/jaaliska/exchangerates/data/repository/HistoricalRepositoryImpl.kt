package com.jaaliska.exchangerates.data.repository

import com.jaaliska.exchangerates.data.historical.datasource.RetrofitHistoricalDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.repository.HistoricalRepository
import kotlinx.coroutines.*
import java.util.*

class HistoricalRepositoryImpl(
    private val retrofitHistoricalDataSource: RetrofitHistoricalDataSource
) : HistoricalRepository {

    override suspend fun getYearHistoryForCurrency(
        year: Int,
        baseCurrency: Currency,
        currenciesRateFor: Currency
    ): List<ExchangeRates> {
        val currentYear = getCurrentYear()
        val month = if (year == currentYear) getCurrentMonth() else 12
        return coroutineScope {
            awaitAll(*(0 until month).map {
                async {
                    getRate(year, it, baseCurrency, currenciesRateFor)
                }
            }.toTypedArray())
        }
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