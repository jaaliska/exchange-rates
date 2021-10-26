package com.jaaliska.exchangerates.domain.repository.exception

import java.lang.Exception

class RatesNotFoundException(
    private val baseCurrencyCode: String,
    cause: Throwable? = null
): Exception(cause) {
    fun getBaseCurrencyCode() = baseCurrencyCode
}