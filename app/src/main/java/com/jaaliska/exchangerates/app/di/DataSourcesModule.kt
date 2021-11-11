package com.jaaliska.exchangerates.app.di

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
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.dsl.module


@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
internal val dataSources = module {

    factory<AnchorCurrencyDataSource> {
        SharedPrefAnchorCurrencyRepository(sharedPref = get())
    }

    factory<CurrenciesDataSource> {
        MediatorCurrenciesDataSource(
            remoteRepository = get(),
            localRepository = get()
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
            localRatesRepository = get(),
            remoteRatesRepository = get(),
            localCurrencyRepository = get(),
            anchorCurrencyRepository = get(),
            alarmService = get(),
            coroutineScope = coroutineScope
        )
    }
}