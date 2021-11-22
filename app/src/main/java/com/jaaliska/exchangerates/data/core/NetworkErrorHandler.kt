package com.jaaliska.exchangerates.data.core

import com.jaaliska.exchangerates.domain.model.GenericError
import com.jaaliska.exchangerates.domain.model.NetworkError
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class NetworkErrorHandler {

    fun mapError(throwable: Throwable): Throwable {
        Timber.e(throwable)
        when (throwable) {
            is IOException -> return NetworkError(throwable.message)
            is HttpException -> {
                return when (throwable.code()) {
                    401 -> GenericError("auth failed")
                    else -> GenericError(throwable.message())
                }
            }
        }
        return throwable
    }
}