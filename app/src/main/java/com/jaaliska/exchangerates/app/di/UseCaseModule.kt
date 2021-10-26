package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.domain.usecases.GetNamedRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import org.koin.dsl.module

internal val useCaseModule = module {
    single {
        GetSupportedCurrenciesUseCase(
            remoteCurrenciesRepository = get(REMOTE),
            localCurrenciesRepository = get(LOCAL)
        )
    }

    single {
        GetNamedRatesUseCase(
            localCurrencyRepository = get(LOCAL),
            favoriteCurrencyRepository = get(LOCAL),
            getRatesUseCase = get()
        )
    }
}