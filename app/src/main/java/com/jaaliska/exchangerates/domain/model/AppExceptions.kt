package com.jaaliska.exchangerates.domain.model

import timber.log.Timber

class NetworkError(message: String? = null) : Exception()
class GenericError(message: String? = null): Exception()

class RatesNotFoundException(
    private val baseCurrencyCode: String,
    cause: Throwable? = null
): Exception(cause) {

    init {
        Timber.e("Rates by $baseCurrencyCode code is absent")
    }
}