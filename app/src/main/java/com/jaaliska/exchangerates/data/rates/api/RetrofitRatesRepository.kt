package com.jaaliska.exchangerates.data.rates.api

import com.jaaliska.exchangerates.data.core.NetworkErrorHandler
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.Rate

class RetrofitRatesRepository(private val api: RatesAPI) {

    private val networkErrorHandler = NetworkErrorHandler()

    suspend fun getRates(anchorCurrency: Currency, currencies: List<Currency>): List<Rate> {
        val symbols = currencies.map(Currency::code).joinToString(",")
        return try {
            api.getLatestRates(baseCurrencyCode = anchorCurrency.code, symbols = symbols)
                .response.rates.map { (code, rate) ->
                    Rate(currency = currencies.find { it.code == code }!!, rate = rate)
                }
        } catch (e: Exception) {
            throw networkErrorHandler.mapError(e)
        }
    }
}



