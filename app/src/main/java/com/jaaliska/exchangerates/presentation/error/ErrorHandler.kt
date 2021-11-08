package com.jaaliska.exchangerates.presentation.error

import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.GenericError
import com.jaaliska.exchangerates.domain.NetworkError

class ErrorHandler {
    fun map(ex: Throwable): Int {
        return when (ex) {
            is NetworkError -> R.string.network_error
            is GenericError -> R.string.something_went_wrong
            else -> R.string.something_went_wrong
        }
    }
}