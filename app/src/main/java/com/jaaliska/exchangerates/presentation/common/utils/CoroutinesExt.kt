package com.jaaliska.exchangerates.presentation.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    handleError: suspend (Exception) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
) {
    launch(context, start) {
        try {
            block()
        } catch (e: Exception) {
            handleError(e)
        }
    }
}