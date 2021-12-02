package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.rates.repository.RoomRatesRepository
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.BaseCurrencyChoiceDialogViewModel
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialogViewModel
import com.jaaliska.exchangerates.presentation.ui.main.BaseHomeViewModel
import com.jaaliska.exchangerates.presentation.ui.main.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModels = module {
    viewModel< BaseHomeViewModel> {
        val roomRatesRepository: RoomRatesRepository = get()
        HomeViewModel(
            ratesDataSource = get(),
            prefsRepository = get(),
            currencies = get(),
            getRatesUpdateDates = roomRatesRepository.getDateChanges()
        )
    }

    viewModel<BaseCurrencyChoiceDialogViewModel> {
        CurrencyChoiceDialogViewModel(
            setFavoriteCurrencies = get(),
            currencies = get()
        )
    }
}