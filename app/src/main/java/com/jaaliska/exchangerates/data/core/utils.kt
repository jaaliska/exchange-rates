package com.jaaliska.exchangerates.data.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun CoroutineScope.registerHooks(vararg flows: Flow<*>, action: suspend () -> Any) {
    flows.forEach { flow ->
        flow.onEach { action() }
            .flowOn(Dispatchers.IO)
            .launchIn(this)
    }
}