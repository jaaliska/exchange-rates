package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.usecase.*
import com.jaaliska.exchangerates.domain.usecases.*
import org.koin.dsl.module

internal val useCaseModule = module {

    single<GetSupportedCurrenciesUseCase> {
        GetSupportedCurrenciesUseCaseImpl(
            remoteCurrencyRepository = get(),
            localCurrenciesRepository = get()
        )
    }

    single<GetNamedRatesUseCase> {
        GetNamedRatesUseCaseImpl(
            localCurrencyRepository = get(),
            getRatesUseCase = get()
        )
    }

    single<GetRatesUseCase> {
        GetRatesUseCaseImpl(
            localRatesRepository = get(),
            localCurrencyRepository = get(),
            refreshRatesUseCase = get()
        )
    }

    single<RefreshRatesUseCase> {
        RefreshRatesUseCaseImpl(
            localRatesRepository = get(),
            remoteRatesRepository = get(),
            localCurrencyRepository = get(),
            alarmService = get()
        )
    }

    single<SetFavoriteCurrenciesUseCase> {
        SetFavoriteCurrenciesUseCaseImpl(
            localCurrencyRepository = get(),
            refreshRatesUseCase = get()
        )
    }
}