package com.jaaliska.exchangerates.app

import android.app.Application
import com.jaaliska.exchangerates.app.di.app
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class ExchangeRatesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ExchangeRatesApp)
            modules(app)
        }
        Timber.plant(Timber.DebugTree())
    }
}