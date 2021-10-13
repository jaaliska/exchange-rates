package com.jaaliska.exchangerates.app.di

import android.app.Application
import android.content.SharedPreferences
import com.jaaliska.exchangerates.data.repository.CurrencyApiRepositoryImpl
import com.jaaliska.exchangerates.data.repository.CurrencyDbRepositoryImpl
import com.jaaliska.exchangerates.data.repository.CurrencyRepositoryImpl
import com.jaaliska.exchangerates.data.repository.PreferencesRepositoryImpl
import com.jaaliska.exchangerates.domain.repository.CurrencyApiRepository
import com.jaaliska.exchangerates.domain.repository.CurrencyDbRepository
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val repositoryModule = module {
    single{ getSharedPrefs(androidApplication()) }
    single<CurrencyApiRepository> { CurrencyApiRepositoryImpl(api = get()) }
    single<CurrencyDbRepository> { CurrencyDbRepositoryImpl(db = get()) }
    single<CurrencyRepository> { CurrencyRepositoryImpl(apiRepository = get(), dbRepository = get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(sharedPreferences = get()) }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("default",  android.content.Context.MODE_PRIVATE)
}