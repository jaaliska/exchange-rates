package com.jaaliska.exchangerates.data.currency.datasource

import com.jaaliska.exchangerates.data.currency.api.CurrencyAPI
import com.jaaliska.exchangerates.data.currency.model.api.CurrencyDto
import com.jaaliska.exchangerates.data.error.NetworkErrorHandler
import com.jaaliska.exchangerates.domain.model.Currency
import kotlinx.coroutines.*
import java.lang.Exception

class RetrofitCurrencyDataSource(
    private val api: CurrencyAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val networkErrorHandler = NetworkErrorHandler()

    suspend fun readSupportedCurrencies(codes: List<String>? = null): List<Currency> {
        val currencies: Map<String, CurrencyDto> = withContext(dispatcher) {
            try {
                api.getSupportedCurrencies().response.fiats
            } catch (e: Exception) {
                throw networkErrorHandler.mapError(e)
            }
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