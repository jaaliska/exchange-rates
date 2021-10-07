package com.jaaliska.exchangerates.domain.repository

import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.ResultWrapper

interface CurrencyRepository {
    suspend fun getExchangeRates(baseCurrency : String) : ResultWrapper<ExchangeRates>
}