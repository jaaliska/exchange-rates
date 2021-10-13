package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.presentation.service.AlarmService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val serviceModule = module {
    single { AlarmService(context = androidContext()) }
}