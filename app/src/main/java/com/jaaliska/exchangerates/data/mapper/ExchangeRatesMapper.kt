package com.jaaliska.exchangerates.data.mapper

import com.jaaliska.exchangerates.data.rates.model.api.ResponseDto
import com.jaaliska.exchangerates.domain.CurrencyNotFoundException
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate

class ExchangeRatesMapper {

    fun map(
        ratesDetails: ResponseDto.RatesDetailsDto,
        baseCurrency: Currency,
        currencyCodes: List<Currency>
    ) = ExchangeRates(
        date = ratesDetails.date,
        baseCurrency = baseCurrency,
        rates = ratesDetails.rates.map {
            Rate(
                currency = Currency(
                    code = it.key,
                    name = currencyCodes.find { currency ->
                        currency.code == it.key
                    }?.name ?: throw CurrencyNotFoundException(it.key)
                ),
                exchangeRate = it.value,
            )
        }
    )
}