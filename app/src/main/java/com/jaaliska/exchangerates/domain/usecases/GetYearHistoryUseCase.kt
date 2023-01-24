package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.repository.HistoricalRepository

class GetYearHistoryUseCase(
    private val repository: HistoricalRepository
) {

    suspend operator fun invoke(
        year: Int,
        baseCurrency: Currency,
        currenciesRateFor: Currency
    ): List<ExchangeRates> {
        return repository.getYearHistoryForCurrency(year, baseCurrency, currenciesRateFor)
    }
}