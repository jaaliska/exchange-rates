package com.jaaliska.exchangerates.presentation.common.error

import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.GenericError
import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.NetworkError

class ErrorHandler {
    fun map(ex: Throwable): Int {
        return when (ex) {
            is NetworkError -> R.string.network_error
            is GenericError -> R.string.something_went_wrong
            is IllegalFavoritesCountException -> R.string.not_enough_changed_currency
            else -> R.string.something_went_wrong
        }
    }
}