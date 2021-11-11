package com.jaaliska.exchangerates

import com.jaaliska.exchangerates.data.mapper.ExchangeRatesMapper
import com.jaaliska.exchangerates.data.model.api.latestRates.RatesDetailsDto
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.domain.model.RatesSnapshot
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class RatesMapperTest {

    private val mapper = ExchangeRatesMapper()
    private val date = Date()
    private var ratesDetailsDto = RatesDetailsDto(
        date,
        "USD",
        mutableMapOf(
            "RUB" to 12.34,
            "BYN" to 23.45,
            "PLN" to 34.56,
            "UAH" to 45.67,
            "EUR" to 56.78
        )
    )

    private var supportedCurrencies = mutableMapOf(
        "RUB" to Currency(code = "RUB", name = "Russian ruble"),
        "BYN" to Currency(code = "BYN", name = "Belarusian ruble"),
        "PLN" to Currency(code = "PLN", name = "Polish złoty"),
        "UAH" to Currency(code = "UAH", name = "Ukrainian hryvnia"),
        "EUR" to Currency(code = "EUR", name = "Euro"),
        "USD" to Currency(code = "USD", name = "United States dollar")
    )

    @Test
    fun `when clear request`() {
        val result = mapper.map(ratesDetailsDto, supportedCurrencies)
        val expect = RatesSnapshot(
            date = date,
            baseCurrencyCode = Currency(code = "USD", name = "United States dollar"),
            listOf(
                Rate("Russian ruble", "RUB", 12.34),
                Rate("Belarusian ruble", "BYN", 23.45),
                Rate("Polish złoty", "PLN", 34.56),
                Rate("Ukrainian hryvnia", "UAH", 45.67),
                Rate("Euro", "EUR", 56.78),
            )
        )
        assertEquals(result, expect)
    }

    @Test
    fun `when some supported currencies are not exist`() {
        supportedCurrencies.remove("UAH")
        supportedCurrencies.remove("RUB")
        supportedCurrencies.remove("USD")
        val result = mapper.map(ratesDetailsDto, supportedCurrencies)
        val expect = RatesSnapshot(
            date = date,
            baseCurrencyCode = Currency(code = "USD", name = ""),
            listOf(
                Rate("", "RUB", 12.34),
                Rate("Belarusian ruble", "BYN", 23.45),
                Rate("Polish złoty", "PLN", 34.56),
                Rate("", "UAH", 45.67),
                Rate("Euro", "EUR", 56.78),
            )
        )
        assertEquals(result, expect)
    }

    @Test
    fun `when some rates are not exist`() {
        val ratesDetailsDto = RatesDetailsDto(
            date,
            "USD",
            mutableMapOf(
                "RUB" to 12.34,
                "EUR" to 56.78
            )
        )
        val result = mapper.map(ratesDetailsDto, supportedCurrencies)
        val expect = RatesSnapshot(
            date = date,
            baseCurrencyCode = Currency(code = "USD", name = "United States dollar"),
            listOf(
                Rate("Russian ruble", "RUB", 12.34),
                Rate("Euro", "EUR", 56.78),
            )
        )
        assertEquals(result, expect)
    }

}