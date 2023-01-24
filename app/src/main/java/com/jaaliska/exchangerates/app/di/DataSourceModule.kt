package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.historical.datasource.RetrofitHistoricalDataSource
import org.koin.dsl.module

internal val dataSourceModule = module {

    single { RetrofitHistoricalDataSource(api = get()) }
}
