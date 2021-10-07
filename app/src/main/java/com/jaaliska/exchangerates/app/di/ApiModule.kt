package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.api.CurrencyAPI
import org.koin.core.module.Module
import retrofit2.Retrofit

internal val provideApis: Module.() -> Unit = {
    single { ratesAPI(get()) }
}

private fun ratesAPI(retrofit: Retrofit): CurrencyAPI =
    retrofit.create(CurrencyAPI::class.java)
