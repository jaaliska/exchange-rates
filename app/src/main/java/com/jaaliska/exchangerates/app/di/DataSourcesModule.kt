package com.jaaliska.exchangerates.app.di

import android.content.Context
import android.content.SharedPreferences
import com.jaaliska.exchangerates.data.anchor_currency.SharedPrefAnchorCurrencyRepository
import com.jaaliska.exchangerates.data.currency.MediatorCurrenciesDataSource
import com.jaaliska.exchangerates.data.rates_snapshot.MediatorRatesDataSource
import com.jaaliska.exchangerates.domain.datasource.AnchorCurrencyDataSource
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.dsl.binds
import org.koin.dsl.module


@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
internal val dataSources = module {

    factory {
        SharedPrefAnchorCurrencyRepository(sharedPref = getSharedPrefs(context = androidContext()))
    }.binds(arrayOf(AnchorCurrencyDataSource::class, SharedPrefAnchorCurrencyRepository::class))

    factory<CurrenciesDataSource> {
        MediatorCurrenciesDataSource(
            remoteRepository = get(),
            dao = get()
        )
    }

    factory<RatesDataSource> {
        val job = Job()
        val coroutineScope = CoroutineScope(job)

        registerCallback(object : ScopeCallback {
            override fun onScopeClose(scope: Scope) {
                job.cancel()
            }
        })

        MediatorRatesDataSource(
            currencyDao = get(),
            remoteRatesRepository = get(),
            ratesSnapshotDao = get(),
            anchorCurrencyRepository = get(),
            alarmService = get(),
            coroutineScope = coroutineScope
        )
    }
}

fun getSharedPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences("default", Context.MODE_PRIVATE)
}