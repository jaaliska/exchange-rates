package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.domain.usecases.GetYearHistoryUseCase
import com.jaaliska.exchangerates.domain.usecases.SetFavoriteCurrenciesUseCase
import org.koin.dsl.module

internal val useCaseModule = module {

    single {
        SetFavoriteCurrenciesUseCase(
            localCurrencyDataSource = get(),
            ratesRepository = get(),
            preferencesRepository = get()
        )
    }

    single {
        GetYearHistoryUseCase(
            repository = get()
        )
    }
}