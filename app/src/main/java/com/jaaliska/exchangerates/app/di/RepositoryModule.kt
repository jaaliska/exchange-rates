package com.jaaliska.exchangerates.app.di

import android.app.Application
import android.content.SharedPreferences
import com.jaaliska.exchangerates.data.repository.SharedPreferencesRepository
import com.jaaliska.exchangerates.data.repository.currency.RetrofitCurrencyRepository
import com.jaaliska.exchangerates.data.repository.currency.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.repository.rates.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.repository.rates.RoomRatesRepository
import com.jaaliska.exchangerates.domain.repository.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val repositoryModule = module {
    single { getSharedPrefs(androidApplication()) }

    single<CurrencyRepository>(LOCAL) { RoomCurrencyRepository(db = get()) }
    single<ReadonlyCurrencyRepository>(REMOTE) { RetrofitCurrencyRepository(api = get()) }
    single<ReadonlyCurrencyRepository>(LOCAL) { RoomCurrencyRepository(db = get()) }

    single<FavoriteCurrenciesRepository>(LOCAL) { RoomCurrencyRepository(db = get()) }

    single<RatesRepository>(LOCAL) { RoomRatesRepository(db = get()) }
    single<ReadonlyRatesRepository>(REMOTE) { RetrofitRatesRepository(api = get()) }
    single<ReadonlyRatesRepository>(LOCAL) { RoomRatesRepository(db = get()) }

    single<PreferencesRepository> { SharedPreferencesRepository(sharedPreferences = get()) }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("default", android.content.Context.MODE_PRIVATE)
}