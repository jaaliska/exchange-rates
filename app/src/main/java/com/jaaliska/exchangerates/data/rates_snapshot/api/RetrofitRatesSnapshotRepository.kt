package com.jaaliska.exchangerates.data.rates_snapshot.api

import com.jaaliska.exchangerates.data.core.NetworkErrorHandler
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.RatesSnapshot

class RetrofitRatesSnapshotRepository(private val api: RatesAPI) {

    private val networkErrorHandler = NetworkErrorHandler()

    suspend fun getRates(
        anchorCurrency: Currency,
        currencies: List<Currency>
    ): RatesSnapshot {
        val symbols = currencies.map(Currency::code).joinToString(",")
        return try {
            val remoteSnapshot = api.getLatestRates(
                baseCurrencyCode = anchorCurrency.code,
                symbols = symbols
            ).response

            RatesSnapshot(
                date = remoteSnapshot.date,
                baseCurrency = anchorCurrency,
                rates = remoteSnapshot.rates.map { (code, rate) ->
                    RatesSnapshot.Rate(
                        currency = currencies.find { it.code == code }!!,
                        rate = rate
                    )
                })
        } catch (e: Exception) {
            throw networkErrorHandler.mapError(e)
        }
    }
}



