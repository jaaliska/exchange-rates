package com.jaaliska.exchangerates.presentation.ui.historical

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoricalViewModel(
    private val currencies: CurrenciesDataSource,
) : BaseHistoricalViewModel() {

    override val favoriteCurrencyCodes = MutableSharedFlow<List<String>>(1)
    override val isLoading = MutableStateFlow<Boolean>(false)
    override val error = MutableSharedFlow<Int?>(0)
    override val selectedYear = MutableStateFlow<Int?>(null)
    private val currencyFrom = MutableStateFlow<String?>(null)
    private val currencyTo = MutableStateFlow<String?>(null)

    private val validSelection: Flow<Triple<String, String, Int>?> =
        combine(currencyFrom, currencyTo, selectedYear) { from, to, year ->
            return@combine if (from != null && to != null && from != to && year != null)
                Triple(from, to, year) else null
        }
    private val sameCurrencies = combine(currencyFrom, currencyTo) { from, to ->
        from != null && from == to
    }

    override fun onYearSelected(year: Int?) {
        selectedYear.value = year
    }

    override fun onCurrencyFromSelected(currencyCode: String?) {
        currencyFrom.value = currencyCode
    }

    override fun onCurrencyToSelected(currencyCode: String?) {
        currencyTo.value = currencyCode
    }

    init {
        viewModelScope.launch {
            launch { favoriteCurrencyCodes.emit(currencies.getFavorite().map { it.code }) }

            launch {
                sameCurrencies.collect {
                    if (it) {
                        error.emit(R.string.select_different_currencies)
                    }
                }
            }

            launch { validSelection.collect { if (it != null) { getChartData() } } }
        }
    }

    private fun getChartData() {
        Log.i("MyCheck HistoricalViewModel", "go to server")
    }
}
