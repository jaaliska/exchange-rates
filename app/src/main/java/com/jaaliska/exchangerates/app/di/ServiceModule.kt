package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.presentation.service.AlarmService
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@DelicateCoroutinesApi
internal val serviceModule = module {
    single { AlarmService(context = androidContext()) }
}