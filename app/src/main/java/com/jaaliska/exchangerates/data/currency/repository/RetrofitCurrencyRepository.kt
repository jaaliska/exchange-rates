package com.jaaliska.exchangerates.data.currency.repository

import com.jaaliska.exchangerates.data.currency.api.CurrencyAPI
import com.jaaliska.exchangerates.data.currency.model.api.CurrencyDto
import com.jaaliska.exchangerates.data.error.NetworkErrorHandler
import com.jaaliska.exchangerates.domain.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class RetrofitCurrencyRepository(
    private val api: CurrencyAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val networkErrorHandler = NetworkErrorHandler()

    suspend fun readSupportedCurrencies(codes: List<String>? = null): List<Currency> {
        val currencies: Map<String, CurrencyDto>
        try {
            currencies = api.getSupportedCurrencies().response.fiats
        } catch (e: Exception) {
            throw networkErrorHandler.mapError(e)
        }

        if (codes != null) {
            return codes.fold(mutableListOf()) { acc, code ->
                currencies[code]?.let {
                    acc.add(
                        Currency(
                            name = it.name,
                            code = it.code
                        )
                    )
                }
                acc
            }
        }

        return currencies.map {
            Currency(
                name = it.value.name,
                code = it.value.code
            )
        }
    }
}