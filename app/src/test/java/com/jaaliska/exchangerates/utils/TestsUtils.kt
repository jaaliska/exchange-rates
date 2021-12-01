package com.jaaliska.exchangerates.utils

import kotlinx.coroutines.runBlocking
import org.junit.Assert

inline fun <reified T : Throwable> assertThrows(noinline executable: suspend () -> Unit): T =
    Assert.assertThrows(T::class.java) {
        runBlocking {
            executable()
        }
    }