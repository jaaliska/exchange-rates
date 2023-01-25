package com.jaaliska.exchangerates.presentation.ui.historical

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.repository.CurrenciesRepository
import com.jaaliska.exchangerates.domain.usecases.GetYearHistoryUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoricalViewModel(
    private val currencies: CurrenciesRepository,
    private val getHistory: GetYearHistoryUseCase
) : BaseHistoricalViewModel() {

    private val favoriteCurrencies = flow {
        emit(currencies.getFavorite())
    }
    override val favoriteCurrencyCodes = favoriteCurrencies.map { it.map { it.code } }
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)
    override val isLoading = MutableStateFlow<Boolean>(false)
    override val error = MutableSharedFlow<Int?>(0)
    override val selectedYear = MutableStateFlow<Int?>(null)
    private val currencyCodeFrom = MutableStateFlow<String?>(null)
    private val currencyCodeTo = MutableStateFlow<String?>(null)

    private val validSelection: Flow<Triple<String, String, Int>?> =
        combine(currencyCodeFrom, currencyCodeTo, selectedYear) { from, to, year ->
            return@combine if (from != null && to != null && from != to && year != null)
                Triple(from, to, year) else null
        }
    private val sameCurrencies = combine(currencyCodeFrom, currencyCodeTo) { from, to ->
        from != null && from == to
    }

    override fun onYearSelected(year: Int?) {
        selectedYear.value = year
    }

    override fun onCurrencyFromSelected(currencyCode: String?) {
        currencyCodeFrom.value = currencyCode
    }

    override fun onCurrencyToSelected(currencyCode: String?) {
        currencyCodeTo.value = currencyCode
    }

    init {
        viewModelScope.launch {
            launch {
                sameCurrencies.collect {
                    if (it) {
                        error.emit(R.string.select_different_currencies)
                    }
                }
            }

            launch {
                validSelection.collect {
                    if (it != null) {
                        getChartData()
                    }
                }
            }
        }
    }

    private suspend fun getChartData() {
        try {
            val res = getHistory(
                selectedYear.value!!,
                getCurrencyByCode(currencyCodeFrom.value!!)!!,
                getCurrencyByCode(currencyCodeTo.value!!)!!
            )
            Log.i("MyCheck", res.toString())
        } catch (e: Exception) {
            error.emit(null)
        }
    }

    private suspend fun getCurrencyByCode(code: String) = favoriteCurrencies.map { it ->
        it.find {
            it.code == code
        }
    }.singleOrNull()
}
