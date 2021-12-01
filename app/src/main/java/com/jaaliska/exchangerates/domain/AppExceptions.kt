package com.jaaliska.exchangerates.domain

import timber.log.Timber
import java.lang.Exception

class NetworkError(message: String? = null) : Exception()
class GenericError(message: String? = null) : Exception()

class CurrencyNotFoundException(
    private val baseCurrencyCode: String,
    cause: Throwable? = null
) : Exception(cause) {
    init {
        Timber.e("Currency by $baseCurrencyCode code is absent")
    }
}

class RatesNotFoundException(
    private val baseCurrencyCode: String,
    cause: Throwable? = null
) : Exception(cause) {

    init {
        Timber.e("Rates by $baseCurrencyCode code is absent")
    }
}

class IllegalFavoritesCountException : Exception()