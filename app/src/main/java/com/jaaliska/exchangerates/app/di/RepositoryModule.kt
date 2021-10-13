package com.jaaliska.exchangerates.app.di

import android.app.Application
import android.content.SharedPreferences
import com.jaaliska.exchangerates.data.repository.CurrencyRepositoryImpl
import com.jaaliska.exchangerates.data.repository.PreferencesRepositoryImpl
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val repositoryModule = module {
    single{ getSharedPrefs(androidApplication()) }
    single<CurrencyRepository> { CurrencyRepositoryImpl(api = get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(sharedPreferences = get()) }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("default",  android.content.Context.MODE_PRIVATE)
}