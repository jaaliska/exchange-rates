package com.jaaliska.exchangerates.presentation.ui.historical

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseHistoricalViewModel : ViewModel() {

    abstract val favoriteCurrencyCodes: SharedFlow<List<String>>
    abstract val isLoading: Flow<Boolean>
    abstract val error: Flow<Int?>
    abstract val selectedYear: StateFlow<Int?>

    abstract fun onYearSelected(year: Int?)
    abstract fun onCurrencyFromSelected(currencyCode: String?)
    abstract fun onCurrencyToSelected(currencyCode: String?)

}