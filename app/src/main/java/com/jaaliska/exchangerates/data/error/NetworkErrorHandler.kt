package com.jaaliska.exchangerates.data.error

import com.jaaliska.exchangerates.domain.model.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class NetworkErrorHandler {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        if (code == 401) {
                            ResultWrapper.GenericError(code, "auth failed")
                        }
                        ResultWrapper.GenericError(code, throwable.message())
                    }
                    else -> {
                        ResultWrapper.GenericError(null, throwable.message)
                    }
                }
            }
        }
    }
}