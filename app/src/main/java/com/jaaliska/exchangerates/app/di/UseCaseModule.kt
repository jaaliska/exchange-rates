package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.datasource.CurrenciesDataSourceImpl
import com.jaaliska.exchangerates.data.datasource.RatesDataSourceImpl
import com.jaaliska.exchangerates.data.usecase.SetFavoriteCurrenciesUseCaseImpl
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.domain.usecases.GetYearHistoryUseCase
import com.jaaliska.exchangerates.domain.usecases.SetFavoriteCurrenciesUseCase
import org.koin.dsl.module

internal val useCaseModule = module {

    single<RatesDataSource> {
        RatesDataSourceImpl(
            localRatesRepository = get(),
            localCurrencyRepository = get(),
            remoteRatesRepository = get(),
            alarmService = get()
        )
    }

    single<SetFavoriteCurrenciesUseCase> {
        SetFavoriteCurrenciesUseCaseImpl(
            localCurrencyRepository = get(),
            ratesDataSource = get(),
            preferencesRepository = get()
        )
    }

    single<CurrenciesDataSource> {
        CurrenciesDataSourceImpl(
            remoteCurrencyRepository = get(),
            localCurrencyRepository = get()
        )
    }

    single {
        GetYearHistoryUseCase(
            repository = get()
        )
    }
}