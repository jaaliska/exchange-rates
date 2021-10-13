package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.presentation.ui.main.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModels = module {
    viewModel {
        HomeViewModel(
            currencyRepository = get(),
            prefsRepository = get(),
            alarmService = get()
        )
    }
}