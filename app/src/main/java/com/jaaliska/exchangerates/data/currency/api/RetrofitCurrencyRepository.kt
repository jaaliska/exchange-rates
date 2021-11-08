package com.jaaliska.exchangerates.data.currency.api

import com.jaaliska.exchangerates.data.core.NetworkErrorHandler
import com.jaaliska.exchangerates.data.currency.api.SupportedCurrenciesResponse.CurrencyDto
import com.jaaliska.exchangerates.domain.model.Currency

class RetrofitCurrencyRepository(private val api: CurrencyAPI) {

    private val networkErrorHandler = NetworkErrorHandler()

    suspend fun readSupportedCurrencies(): List<Currency> {
        return try {
            api.getSupportedCurrencies().response.fiats.values.map(CurrencyDto::toDomain)
        } catch (e: Exception) {
            throw networkErrorHandler.mapError(e)
        }
    }
}