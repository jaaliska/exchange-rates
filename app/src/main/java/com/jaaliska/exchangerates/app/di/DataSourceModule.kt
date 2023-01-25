package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.currency.datasource.RetrofitCurrencyDataSource
import com.jaaliska.exchangerates.data.currency.datasource.RoomCurrencyDataSource
import com.jaaliska.exchangerates.data.historical.datasource.RetrofitHistoricalDataSource
import com.jaaliska.exchangerates.data.rates.datasource.RetrofitRatesDataSource
import com.jaaliska.exchangerates.data.rates.datasource.RoomRatesDataSource
import org.koin.dsl.module

internal val dataSourceModule = module {

    single { RetrofitHistoricalDataSource(api = get()) }
    single { RoomCurrencyDataSource(db = get()) }
    single { RetrofitCurrencyDataSource(api = get()) }
    single { RetrofitRatesDataSource(api = get()) }
    single { RoomRatesDataSource(db = get()) }
}
