package com.jaaliska.exchangerates.data.repository

import com.jaaliska.exchangerates.data.api.CurrencyAPI
import com.jaaliska.exchangerates.data.error.NetworkErrorHandler
import com.jaaliska.exchangerates.data.mapper.ExchangeRatesMapper
import com.jaaliska.exchangerates.data.model.latestRates.RatesDetailsDto
import com.jaaliska.exchangerates.domain.model.CurrencyDetails
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.ResultWrapper
import com.jaaliska.exchangerates.domain.model.ResultWrapper.GenericError
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import kotlinx.coroutines.*

class CurrencyRepositoryImpl(
    private val api: CurrencyAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrencyRepository {

    private val networkErrorHandler = NetworkErrorHandler()
    private val exchangeRatesMapper = ExchangeRatesMapper()
    private var exchangeRatesCache: ExchangeRates? = null
    private var exchangeRatesCacheBaseCurrencyCode: String? = null

    override suspend fun getExchangeRates(baseCurrency: String, forceUpdate: Boolean): ResultWrapper<ExchangeRates> {
        val cachedRates = exchangeRatesCache
        if (!forceUpdate &&
            exchangeRatesCacheBaseCurrencyCode == baseCurrency &&
            cachedRates != null
        ) {
            return ResultWrapper.Success(cachedRates)
        }

        return loadExchangeRates(baseCurrency)
    }

    private suspend fun loadExchangeRates(baseCurrency: String) = coroutineScope {
        val supportedCurrencies = async { getSupportedCurrencies() }
        val latestRates = async { getLatestRates(baseCurrency) }
        val supportedCurrenciesResult = supportedCurrencies.await()
        val latestRatesResult = latestRates.await()

        if (supportedCurrenciesResult is ResultWrapper.Success &&
            latestRatesResult is ResultWrapper.Success
        ) {
            val result = ResultWrapper.Success(
                exchangeRatesMapper.map(
                    latestRatesResult.value, supportedCurrenciesResult.value
                )
            )
            exchangeRatesCache = result.value
            exchangeRatesCacheBaseCurrencyCode = baseCurrency
            return@coroutineScope result
        }

        listOf(supportedCurrenciesResult, latestRatesResult).forEach {
            if (it is GenericError) {
                return@coroutineScope it
            }
            if (it is ResultWrapper.NetworkError) {
                return@coroutineScope it
            }
        }

        throw RuntimeException("This code is not reachable")
    }

    private suspend fun getSupportedCurrencies(): ResultWrapper<Map<String, CurrencyDetails>> {
        return networkErrorHandler.safeApiCall(dispatcher) {
            api.getSupportedCurrencies().response.fiats
        }
    }

    private suspend fun getLatestRates(baseCurrency: String): ResultWrapper<RatesDetailsDto> {
        return networkErrorHandler.safeApiCall(dispatcher) {
            api.getLatestRates(baseCurrency).response
        }
    }
}



