package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.presentation.ui.MainActivityViewModel
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialogViewModel
import com.jaaliska.exchangerates.presentation.ui.main.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModels = module {
    viewModel {
        HomeViewModel(
            getNamedRatesUseCase = get(),
            refreshRatesUseCase = get(),
            prefsRepository = get(),
            favoriteCurrenciesUseCase = get()
        )
    }
    viewModel {
        MainActivityViewModel(
            prefsRepository = get()
        )
    }
    viewModel {
        CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase = get(),
            favoriteCurrenciesUseCase = get()
        )
    }
}