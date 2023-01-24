package com.jaaliska.exchangerates.app.di

import android.app.Application
import android.content.SharedPreferences
import com.jaaliska.exchangerates.data.currency.repository.RetrofitCurrencyRepository
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.historical.repository.HistoricalRepositoryImpl
import com.jaaliska.exchangerates.data.rates.repository.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.rates.repository.RoomRatesRepository
import com.jaaliska.exchangerates.data.repository.SharedPreferencesRepository
import com.jaaliska.exchangerates.domain.repository.HistoricalRepository
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val repositoryModule = module {
    single { getSharedPrefs(androidApplication()) }
    single<PreferencesRepository> { SharedPreferencesRepository(sharedPreferences = get()) }

    single { RetrofitRatesRepository(api = get()) }
    single { RoomRatesRepository(db = get()) }
    single { RetrofitCurrencyRepository(api = get()) }
    single { RoomCurrencyRepository(db = get()) }
    single<HistoricalRepository> { HistoricalRepositoryImpl(retrofitHistoricalDataSource = get()) }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("default", android.content.Context.MODE_PRIVATE)
}