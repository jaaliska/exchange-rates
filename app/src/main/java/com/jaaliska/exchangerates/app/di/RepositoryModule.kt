package com.jaaliska.exchangerates.app.di

import android.app.Application
import android.content.SharedPreferences
import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.currency.CurrencyDataSource
import com.jaaliska.exchangerates.data.currency.api.RetrofitCurrencyRepository
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.api.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.rates.dao.RateDao
import com.jaaliska.exchangerates.data.rates.dao.RoomRatesRepository
import com.jaaliska.exchangerates.data.shared_pref.SharedPrefAnchorCurrencyRepository
import com.jaaliska.exchangerates.domain.repository.AnchorCurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal val repositoryModule = module {
    single<AnchorCurrencyRepository> {
        SharedPrefAnchorCurrencyRepository(
            sharedPref = getSharedPrefs(
                androidApplication()
            )
        )
    }
    single<RateDao> { get<ExchangeRatesDatabase>().ratesDao() }

    single { RetrofitRatesRepository(api = get()) }
    single { RoomRatesRepository(ratesDao = get()) }
    single { RetrofitCurrencyRepository(api = get()) }
    single { RoomCurrencyRepository(db = get()) }
    single { CurrencyDataSource(remoteRepository = get(), localRepository = get()) }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("default", android.content.Context.MODE_PRIVATE)
}