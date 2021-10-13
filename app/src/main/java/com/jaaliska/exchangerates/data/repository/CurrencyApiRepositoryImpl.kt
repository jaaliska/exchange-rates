package com.jaaliska.exchangerates.data.repository

import com.jaaliska.exchangerates.data.api.CurrencyAPI
import com.jaaliska.exchangerates.data.error.NetworkErrorHandler
import com.jaaliska.exchangerates.data.mapper.ExchangeRatesMapper
import com.jaaliska.exchangerates.data.model.latestRates.RatesDetailsDto
import com.jaaliska.exchangerates.domain.model.CurrencyDetails
import com.jaaliska.exchangerates.domain.model.ResultWrapper
import com.jaaliska.exchangerates.domain.model.ResultWrapper.GenericError
import com.jaaliska.exchangerates.domain.repository.CurrencyApiRepository
import kotlinx.coroutines.*

class CurrencyApiRepositoryImpl(
    private val api: CurrencyAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrencyApiRepository {

    private val networkErrorHandler = NetworkErrorHandler()
    private val exchangeRatesMapper = ExchangeRatesMapper()

    override suspend fun getExchangeRates(baseCurrency: String) = coroutineScope {
        val supportedCurrencies = async { getSupportedCurrencies() }
        val latestRates = async { getLatestRates(baseCurrency) }
        val supportedCurrenciesResult = supportedCurrencies.await()
        val latestRatesResult = latestRates.await()

        if (supportedCurrenciesResult is ResultWrapper.Success &&
            latestRatesResult is ResultWrapper.Success
        ) {
            return@coroutineScope ResultWrapper.Success(
                exchangeRatesMapper.map(
                    latestRatesResult.value, supportedCurrenciesResult.value
                )
            )
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



