package com.jaaliska.exchangerates.app.di

import android.app.Application
import android.content.SharedPreferences
import com.jaaliska.exchangerates.data.repository.CurrenciesRepositoryImpl
import com.jaaliska.exchangerates.data.repository.RatesRepositoryImpl
import com.jaaliska.exchangerates.data.repository.HistoricalRepositoryImpl
import com.jaaliska.exchangerates.data.repository.SharedPreferencesRepository
import com.jaaliska.exchangerates.domain.repository.RatesRepository
import com.jaaliska.exchangerates.domain.repository.CurrenciesRepository
import com.jaaliska.exchangerates.domain.repository.HistoricalRepository
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val repositoryModule = module {

    single { getSharedPrefs(androidApplication()) }

    single<PreferencesRepository> { SharedPreferencesRepository(sharedPreferences = get()) }

    single<HistoricalRepository> { HistoricalRepositoryImpl(retrofitHistoricalDataSource = get()) }

    single<CurrenciesRepository> {
        CurrenciesRepositoryImpl(
            remoteCurrencyDataSource = get(),
            localCurrencyDataSource = get()
        )
    }

    single<RatesRepository> {
        RatesRepositoryImpl(
            localRatesDataSource = get(),
            localCurrencyDataSource = get(),
            remoteRatesDataSource = get(),
            alarmService = get()
        )
    }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("default", android.content.Context.MODE_PRIVATE)
}