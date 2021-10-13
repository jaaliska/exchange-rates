package com.jaaliska.exchangerates.app.di

import androidx.room.Room
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val DB_NAME = "exchange_rates_db"
internal val dataModule = module {
    single<ExchangeRatesDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            ExchangeRatesDatabase::class.java,
            DB_NAME
        ).build()
    }
}