package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.repository.RatesRepository
import com.jaaliska.exchangerates.domain.repository.ReadonlyRatesRepository
import com.jaaliska.exchangerates.domain.repository.exception.RatesNotFoundException

class GetRatesUseCase(
    private val localRatesRepository: RatesRepository,
    private val remoteRatesRepository: ReadonlyRatesRepository,
) {
    suspend operator fun invoke(baseCurrencyCode: String, currencyCodes: List<String>): ExchangeRates {
        return try {
            localRatesRepository.getRates(baseCurrencyCode, currencyCodes)
        } catch (ex: RatesNotFoundException) {
            val rates = remoteRatesRepository.getRates(baseCurrencyCode, currencyCodes)
            localRatesRepository.saveRates(rates)
            rates
        }
    }
}