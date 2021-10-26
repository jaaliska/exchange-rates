package com.jaaliska.exchangerates.data.repository.rates

import com.jaaliska.exchangerates.data.api.CurrencyAPI
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.domain.repository.ReadonlyRatesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RetrofitRatesRepository(
    private val api: CurrencyAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReadonlyRatesRepository {

    override suspend fun getRates(
        baseCurrencyCode: String,
        currencyCodes: List<String>
    ): ExchangeRates {
        val latestRates = api.getLatestRates(baseCurrencyCode, currencyCodes)

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



