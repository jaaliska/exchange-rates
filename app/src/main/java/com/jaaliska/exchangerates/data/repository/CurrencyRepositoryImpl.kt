package com.jaaliska.exchangerates.data.repository

import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.ResultWrapper
import com.jaaliska.exchangerates.domain.repository.CurrencyApiRepository
import com.jaaliska.exchangerates.domain.repository.CurrencyDbRepository
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository

class CurrencyRepositoryImpl(
    private val apiRepository: CurrencyApiRepository,
    private val dbRepository: CurrencyDbRepository
) : CurrencyRepository {

    override suspend fun getExchangeRates(baseCurrencyCode: String, forceUpdate: Boolean): ResultWrapper<ExchangeRates> {
        if (!forceUpdate) {
            val dbResult = dbRepository.getExchangeRates(baseCurrencyCode)
            if (dbResult != null) {
                return ResultWrapper.Success(dbResult)
            }
        }
        val apiResult = apiRepository.getExchangeRates(baseCurrencyCode)
        if (apiResult is ResultWrapper.Success) {
            dbRepository.saveExchangeRates(apiResult.value)
        }
        return apiResult
    }

}