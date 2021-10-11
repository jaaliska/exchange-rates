package com.jaaliska.exchangerates.presentation.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.domain.model.CurrencyDetails
import com.jaaliska.exchangerates.domain.model.CurrencyExchangeRate
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.ResultWrapper
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: CurrencyRepository
) : ViewModel() {

    val exchangeRates = MutableStateFlow<List<CurrencyExchangeRate>>(listOf())
    val baseCurrencyAmount = MutableStateFlow<Double>(DEFAULT_BASE_CURRENCY_AMOUNT)
    val baseCurrencyDetails = MutableStateFlow<CurrencyDetails?>(null)

    init {
        updateExchangeRates("USD")
    }

    fun onCurrencySelection(currencyCode: String, amount: Double) {
        updateExchangeRates(currencyCode, amount)
    }

    private fun updateExchangeRates(baseCurrencyCode: String, amount: Double? = null) {
        viewModelScope.launch {
            when (val exchangeRatesResult = repository.getExchangeRates(baseCurrencyCode)) {
                is ResultWrapper.NetworkError -> Log.d(
                    "HomeViewModel",
                    exchangeRatesResult.toString()
                )
                is ResultWrapper.GenericError -> Log.d(
                    "HomeViewModel",
                    exchangeRatesResult.toString()
                )
                is ResultWrapper.Success<ExchangeRates> -> {
                    Log.d("HomeViewModel", exchangeRatesResult.toString())
                    exchangeRates.value = exchangeRatesResult.value.rates
                    baseCurrencyDetails.emit(exchangeRatesResult.value.baseCurrency)
                    if (amount != null) {
                        baseCurrencyAmount.emit(amount)
                    }
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_BASE_CURRENCY_AMOUNT = 1.0
    }
}
