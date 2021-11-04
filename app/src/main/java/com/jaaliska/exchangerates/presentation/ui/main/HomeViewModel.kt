package com.jaaliska.exchangerates.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.GenericError
import com.jaaliska.exchangerates.domain.NetworkError
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetNamedRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import com.jaaliska.exchangerates.presentation.model.NamedExchangeRates
import com.jaaliska.exchangerates.presentation.model.NamedRate
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog
import com.jaaliska.exchangerates.presentation.utils.doOnError
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val getNamedRatesUseCase: GetNamedRatesUseCase,
    private val refreshRatesUseCase: RefreshRatesUseCase,
    private val prefsRepository: PreferencesRepository,
    private val favoriteCurrenciesUseCase: FavoriteCurrenciesUseCase,
) : ViewModel() {

    val exchangeRates = MutableStateFlow<List<NamedRate>>(listOf())
    val baseCurrencyAmount = MutableStateFlow<Double>(DEFAULT_BASE_CURRENCY_AMOUNT)
    val baseCurrencyDetails = MutableStateFlow<Currency?>(null)
    val updateDate = MutableStateFlow<Date?>(null)
    val isLoading = MutableStateFlow<Boolean>(false)
    val errors = MutableSharedFlow<Int>(0)
    val currencyChoiceDialog = MutableSharedFlow<CurrencyChoiceDialog>(0)
    private var updateExchangeRatesJob: Job? = null

    init {
        viewModelScope.launch {
            val favorites = favoriteCurrenciesUseCase.get()
            if (favorites.isNotEmpty()) {
                updateExchangeRates(prefsRepository.getBaseCurrencyCode())
            } else {
                showCurrencyChoiceDialog()
            }
        }
    }

    private fun showCurrencyChoiceDialog() {
        viewModelScope.launch {
            val dialog = CurrencyChoiceDialog()
            currencyChoiceDialog.emit(dialog)
        }
    }

    fun onCurrencySelection(currencyCode: String, amount: Double) {
        updateExchangeRates(currencyCode) {
            baseCurrencyAmount.value = amount
        }
    }

    fun onSwipeToRefresh() {
        viewModelScope.launch {
            isLoading.emit(true)
            refreshRatesUseCase()
            isLoading.emit(false)
        }
    }

    private fun updateExchangeRates(
        baseCurrencyCode: String,
        onFinished: (() -> Unit)? = null
    ) {
        updateExchangeRatesJob?.cancel()
        updateExchangeRatesJob = viewModelScope.launch {
            isLoading.emit(true)
            getNamedRatesUseCase(baseCurrencyCode)
                .doOnError {
                    val messageRes: Int = when (it) {
                        is NetworkError -> R.string.network_error
                        is GenericError -> R.string.something_went_wrong
                        else -> R.string.something_went_wrong
                    }
                    launch {
                        errors.emit(messageRes)
                    }
                }
                .collect {
                    prefsRepository.setBaseCurrencyCode(it.baseCurrency.code)
                    //   if(baseCurrencyCode == "" || baseCurrencyCode == it.baseCurrency.code) {
                    applyExchangeRatesToScreen(it)
                    // }
                    if (onFinished != null) {
                        onFinished()
                    }
                    isLoading.emit(false)
                }
        }
    }

    private fun applyExchangeRatesToScreen(value: NamedExchangeRates) {
        exchangeRates.value = value.rates.map {
            NamedRate(
                currencyCode = it.first.code,
                currencyName = it.first.name,
                exchangeRate = it.second
            )
        }
        baseCurrencyDetails.value = value.baseCurrency
        updateDate.value = value.date
    }

    companion object {
        private const val DEFAULT_BASE_CURRENCY_AMOUNT = 1.0
    }
}
