package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.currency.api.CurrencyAPI
import com.jaaliska.exchangerates.data.historical.api.HistoricalApi
import com.jaaliska.exchangerates.data.rates.api.RatesAPI
import org.koin.core.module.Module
import retrofit2.Retrofit

internal val provideApis: Module.() -> Unit = {
    single { ratesAPI(get()) }
    single { currencyAPI(get()) }
    single { historicalApi(get()) }
}

private fun ratesAPI(retrofit: Retrofit): RatesAPI =
    retrofit.create(RatesAPI::class.java)

private fun currencyAPI(retrofit: Retrofit): CurrencyAPI =
    retrofit.create(CurrencyAPI::class.java)

private fun historicalApi(retrofit: Retrofit): HistoricalApi =
    retrofit.create(HistoricalApi::class.java)
