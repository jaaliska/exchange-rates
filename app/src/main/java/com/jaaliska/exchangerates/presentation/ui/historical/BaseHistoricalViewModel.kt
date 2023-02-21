package com.jaaliska.exchangerates.presentation.ui.historical

import androidx.lifecycle.ViewModel
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import kotlinx.coroutines.flow.Flow

abstract class BaseHistoricalViewModel : ViewModel() {

    abstract val favoriteCurrencyCodes: Flow<List<String>>
    abstract val currenciesForChart: Flow<List<ExchangeRates>?>
    abstract val isLoading: Flow<Boolean>
    abstract val error: Flow<Int?>
    abstract val selectedYear: Flow<Int?>

    abstract fun onYearSelected(year: Int?)
    abstract fun onCurrencyFromSelected(currencyCode: String?)
    abstract fun onCurrencyToSelected(currencyCode: String?)

}