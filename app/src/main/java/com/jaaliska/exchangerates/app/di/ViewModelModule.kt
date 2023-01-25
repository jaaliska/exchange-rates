package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.rates.datasource.RoomRatesDataSource
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.BaseCurrencyChoiceDialogViewModel
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialogViewModel
import com.jaaliska.exchangerates.presentation.ui.historical.BaseHistoricalViewModel
import com.jaaliska.exchangerates.presentation.ui.historical.HistoricalViewModel
import com.jaaliska.exchangerates.presentation.ui.rates.BaseRatesViewModel
import com.jaaliska.exchangerates.presentation.ui.rates.RatesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModels = module {
    viewModel<BaseRatesViewModel> {
        val roomRatesDataSource: RoomRatesDataSource = get()
        RatesViewModel(
            ratesRepository = get(),
            prefsRepository = get(),
            currencies = get(),
            getRatesUpdateDates = roomRatesDataSource.getDateChanges()
        )
    }

    viewModel<BaseCurrencyChoiceDialogViewModel> {
        CurrencyChoiceDialogViewModel(
            setFavoriteCurrencies = get(),
            currencies = get()
        )
    }
     viewModel<BaseHistoricalViewModel> {
         HistoricalViewModel(
             currencies = get(),
             getHistory = get()
         )
    }
}