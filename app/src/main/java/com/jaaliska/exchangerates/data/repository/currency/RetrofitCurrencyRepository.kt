package com.jaaliska.exchangerates.data.repository.currency

import com.jaaliska.exchangerates.data.api.CurrencyAPI
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.ReadonlyCurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RetrofitCurrencyRepository(
    private val api: CurrencyAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReadonlyCurrencyRepository {

    override suspend fun readSupportedCurrencies(codes: List<String>?): List<Currency> {
        val currencies = api.getSupportedCurrencies().response.fiats

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