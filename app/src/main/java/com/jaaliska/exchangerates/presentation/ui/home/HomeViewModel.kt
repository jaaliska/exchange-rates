package com.jaaliska.exchangerates.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.domain.model.*
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecase.SetAnchorCurrencyUseCase
import com.jaaliska.exchangerates.presentation.utils.doOnError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val ratesDataSource: RatesDataSource,
    private val setAnchorCurrencyUseCase: SetAnchorCurrencyUseCase
) : ViewModel() {

    val exchangeRates = MutableStateFlow<List<Rate>>(listOf())
    val baseCurrencyAmount = MutableStateFlow(DEFAULT_BASE_CURRENCY_AMOUNT)
    val baseCurrencyDetails = MutableStateFlow<Currency?>(null)
    val updateDate = MutableStateFlow<Date?>(null)
    val isLoading = MutableStateFlow(false)
    val error = MutableStateFlow<Int?>(null)

    init {
        ratesDataSource.observe()
            .doOnError {
                val messageRes: Int = when (it) {
                    is NetworkError -> R.string.network_error
                    is GenericError -> R.string.something_went_wrong
                    else -> R.string.something_went_wrong
                }
                error.emit(messageRes)
            }
            .onEach(::applyExchangeRatesToScreen)
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun onCurrencySelection(currency: Currency) {
        viewModelScope.launch(Dispatchers.IO) { setAnchorCurrencyUseCase(currency) }
    }

    fun onSwipeToRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.emit(true)
            ratesDataSource.refresh()
            isLoading.emit(false)
        }
    }

    private fun applyExchangeRatesToScreen(value: ExchangeRates?) {
        exchangeRates.value = value?.rates ?: listOf()
        baseCurrencyDetails.value = value?.baseCurrency
        updateDate.value = value?.date
    }

    companion object {
        private const val DEFAULT_BASE_CURRENCY_AMOUNT = 1.0
    }
}
