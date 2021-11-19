package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.presentation.ui.screens.currency_choice.BaseCurrencyChoiceViewModel
import com.jaaliska.exchangerates.presentation.ui.screens.currency_choice.CurrencyChoiceDialogViewModel
import com.jaaliska.exchangerates.presentation.ui.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModels = module {
    viewModel {
        HomeViewModel(
            ratesDataSource = get(),
            setAnchorCurrencyUseCase = get()
        )
    }
    viewModel<BaseCurrencyChoiceViewModel> {
        CurrencyChoiceDialogViewModel(
            currenciesDataSource = get(),
            updateCurrencyFavoriteStateUseCase = get()
        )
    }
}