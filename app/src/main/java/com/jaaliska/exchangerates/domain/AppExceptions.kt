package com.jaaliska.exchangerates.domain

import java.lang.Exception

class NetworkError(message: String? = null) : Exception()
class GenericError(message: String? = null): Exception()
class RatesNotFoundException(
    private val baseCurrencyCode: String,
    cause: Throwable? = null
): Exception(cause) {
    fun getBaseCurrencyCode() = baseCurrencyCode
}