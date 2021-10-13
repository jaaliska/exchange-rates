package com.jaaliska.exchangerates.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.model.CurrencyDetails
import com.jaaliska.exchangerates.domain.model.CurrencyExchangeRate
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.ResultWrapper
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.presentation.service.AlarmService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val currencyRepository: CurrencyRepository,
    private val prefsRepository: PreferencesRepository,
    private val alarmService: AlarmService
) : ViewModel() {

    val exchangeRates = MutableStateFlow<List<CurrencyExchangeRate>>(listOf())
    val baseCurrencyAmount = MutableStateFlow<Double>(DEFAULT_BASE_CURRENCY_AMOUNT)
    val baseCurrencyDetails = MutableStateFlow<CurrencyDetails?>(null)
    val updateDate = MutableStateFlow<Date?>(null)
    val isLoading = MutableStateFlow<Boolean>(false)
    val errors = MutableSharedFlow<Int>(0)

    init {
        updateExchangeRates(prefsRepository.getBaseCurrencyCode(), false)
        viewModelScope.launch {
            alarmService.exchangeRatesCacheUpdated.collect {
                val currentBaseCurrency = baseCurrencyDetails.value
                if (currentBaseCurrency != null &&
                    currentBaseCurrency.currencyCode == it.baseCurrency.currencyCode
                ) {
                    applyExchangeRatesToScreen(it)
                }
            }
        }
    }

    fun onCurrencySelection(currencyCode: String, amount: Double) {
        updateExchangeRates(currencyCode, false) {
            baseCurrencyAmount.value = amount
        }
    }

    fun onSwipeToRefresh() {
        val currencyCode = baseCurrencyDetails.value?.currencyCode ?: prefsRepository.getBaseCurrencyCode()
        updateExchangeRates(currencyCode, true)
    }

    private fun updateExchangeRates(
        baseCurrencyCode: String,
        forceUpdate: Boolean,
        onFinished: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            isLoading.emit(true)
            val exchangeRatesResult =
                currencyRepository.getExchangeRates(baseCurrencyCode, forceUpdate)
            when (exchangeRatesResult) {
                is ResultWrapper.NetworkError -> {
                    errors.emit(R.string.network_error)
                }
                is ResultWrapper.GenericError -> {
                    errors.emit(R.string.something_went_wrong)
                }
                is ResultWrapper.Success<ExchangeRates> -> {
                    applyExchangeRatesToScreen(exchangeRatesResult.value)
                    prefsRepository.setBaseCurrencyCode(exchangeRatesResult.value.baseCurrency.currencyCode)
                    if (onFinished != null) {
                        onFinished()
                    }
                    alarmService.startAlarm()
                }
            }
            isLoading.emit(false)
        }
    }

    private fun applyExchangeRatesToScreen(value: ExchangeRates) {
        exchangeRates.value = value.rates
        baseCurrencyDetails.value = value.baseCurrency
        updateDate.value = value.date
    }

    companion object {
        private const val DEFAULT_BASE_CURRENCY_AMOUNT = 1.0
    }
}
