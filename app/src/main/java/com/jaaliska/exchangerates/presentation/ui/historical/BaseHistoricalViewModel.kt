package com.jaaliska.exchangerates.presentation.ui.historical

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseHistoricalViewModel : ViewModel() {

    abstract val selectableYear: MutableStateFlow<Int?>
    abstract val currencyFrom: MutableStateFlow<String?>
    abstract val currencyTo: MutableStateFlow<String?>

}