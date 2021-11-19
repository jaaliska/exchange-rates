package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.app.persistence.ExchangeRatesDatabase
import com.jaaliska.exchangerates.data.currency.MediatorCurrenciesDataSource
import com.jaaliska.exchangerates.data.currency.api.RetrofitCurrencyRepository
import com.jaaliska.exchangerates.data.currency.dao.CurrencyDao
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates_snapshot.api.RetrofitRatesSnapshotRepository
import com.jaaliska.exchangerates.data.rates_snapshot.dao.RatesSnapshotDao
import com.jaaliska.exchangerates.data.rates_snapshot.dao.RoomRatesSnapshotRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal val repositoryModule = module {
    single<RatesSnapshotDao> { get<ExchangeRatesDatabase>().ratesSnapshotDao() }
    single<CurrencyDao> { get<ExchangeRatesDatabase>().currencyDao() }

    single { RetrofitRatesSnapshotRepository(api = get()) }
    single { RoomRatesSnapshotRepository(ratesSnapshotDao = get()) }
    single { RetrofitCurrencyRepository(api = get()) }
    single { RoomCurrencyRepository(db = get()) }
    single { MediatorCurrenciesDataSource(remoteRepository = get(), localRepository = get()) }
}