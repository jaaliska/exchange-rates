package com.jaaliska.exchangerates.presentation.ui.historical

import kotlinx.coroutines.flow.MutableStateFlow

class HistoricalViewModel() : BaseHistoricalViewModel() {

    override val selectableYear = MutableStateFlow<Int?>(null)
    override val currencyFrom = MutableStateFlow<String?>(null)
    override val currencyTo = MutableStateFlow<String?>(null)

}