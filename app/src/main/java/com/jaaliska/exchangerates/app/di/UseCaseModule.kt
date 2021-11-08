package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.usecase.FavoriteCurrenciesUseCaseImpl
import com.jaaliska.exchangerates.data.usecase.GetNamedRatesUseCaseImpl
import com.jaaliska.exchangerates.data.usecase.GetSupportedCurrenciesUseCaseImpl
import com.jaaliska.exchangerates.data.usecase.RefreshRatesUseCaseImpl
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal val useCaseModule = module {

    factory<GetSupportedCurrenciesUseCase> {
        GetSupportedCurrenciesUseCaseImpl(
            currencyDataSource = get()
        )
    }

    factory<GetRatesUseCase> {
        GetNamedRatesUseCaseImpl(
            localCurrencyRepository = get(),
            remoteRatesRepository = get(),
            anchorCurrencyRepository = get(),
            localRatesRepository = get()
        )
    }

    factory<RefreshRatesUseCase> {
        RefreshRatesUseCaseImpl(
            localRatesRepository = get(),
            remoteRatesRepository = get(),
            localCurrencyRepository = get(),
            alarmService = get()
        )
    }

    factory<FavoriteCurrenciesUseCase> {
        FavoriteCurrenciesUseCaseImpl(
            localCurrencyRepository = get()
        )
    }
}