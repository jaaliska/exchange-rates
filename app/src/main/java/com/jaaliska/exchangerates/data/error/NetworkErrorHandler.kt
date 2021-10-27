package com.jaaliska.exchangerates.data.error

import com.jaaliska.exchangerates.domain.GenericError
import com.jaaliska.exchangerates.domain.NetworkError
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