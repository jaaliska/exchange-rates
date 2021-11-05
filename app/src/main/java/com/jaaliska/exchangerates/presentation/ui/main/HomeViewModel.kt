package com.jaaliska.exchangerates.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.data.rates.repository.BaseCurrencyCode
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetNamedRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import com.jaaliska.exchangerates.presentation.model.NamedExchangeRates
import com.jaaliska.exchangerates.presentation.model.NamedRate
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog
import kotlinx.coroutines.flow.Flow
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
    private val getRatesUpdateDates: Flow<Map<BaseCurrencyCode, Date>>
) : ViewModel() {

    val exchangeRates = MutableStateFlow<List<NamedRate>>(listOf())
    val baseCurrencyAmount = MutableStateFlow<Double>(DEFAULT_BASE_CURRENCY_AMOUNT)
    val baseCurrencyDetails = MutableStateFlow<Currency?>(null)
    val updateDate = MutableStateFlow<Date?>(null)
    val isLoading = MutableStateFlow<Boolean>(false)
    val errors = MutableSharedFlow<Int>(0)
    val currencyChoiceDialog = MutableSharedFlow<CurrencyChoiceDialog>(0)

    init {
        viewModelScope.launch {
            val favorites = favoriteCurrenciesUseCase.get()
            if (favorites.isNotEmpty()) {
                updateExchangeRates(prefsRepository.getBaseCurrencyCode())
            } else {
                showCurrencyChoiceDialog()
            }
            getRatesUpdateDates
                .collect {
                    val baseCurrencyCode = prefsRepository.getBaseCurrencyCode()
                    val date: Date? = it[baseCurrencyCode]
                    if (date != null && (updateDate.value == null || date > updateDate.value)) {
                        updateExchangeRates(baseCurrencyCode)
                    }
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
        viewModelScope.launch {
            isLoading.emit(true)
            getNamedRatesUseCase(baseCurrencyCode).apply {
                prefsRepository.setBaseCurrencyCode(this.baseCurrency.code)
                applyExchangeRatesToScreen(this)
            }
            if (onFinished != null) {
                onFinished()
            }
            isLoading.emit(false)
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
