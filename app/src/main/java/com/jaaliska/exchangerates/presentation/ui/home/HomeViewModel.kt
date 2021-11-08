package com.jaaliska.exchangerates.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.GenericError
import com.jaaliska.exchangerates.domain.NetworkError
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.domain.repository.AnchorCurrencyRepository
import com.jaaliska.exchangerates.domain.usecases.GetRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import com.jaaliska.exchangerates.presentation.utils.doOnError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    getNamedRatesUseCase: GetRatesUseCase,
    private val refreshRatesUseCase: RefreshRatesUseCase,
    private val prefsRepository: AnchorCurrencyRepository
) : ViewModel() {

    val exchangeRates = MutableStateFlow<List<Rate>>(listOf())
    val baseCurrencyAmount = MutableStateFlow(DEFAULT_BASE_CURRENCY_AMOUNT)
    val baseCurrencyDetails = MutableStateFlow<Currency?>(null)
    val updateDate = MutableStateFlow<Date?>(null)
    val isLoading = MutableStateFlow(false)
    val errors = MutableSharedFlow<Int>(0)

    init {
        getNamedRatesUseCase()
            .doOnError {
                val messageRes: Int = when (it) {
                    is NetworkError -> R.string.network_error
                    is GenericError -> R.string.something_went_wrong
                    else -> R.string.something_went_wrong
                }
                errors.emit(messageRes)
            }
            .flowOn(Dispatchers.IO)
            .onEach { it?.let(::applyExchangeRatesToScreen) }
            .launchIn(viewModelScope)
    }

    fun onCurrencySelection(currencyCode: String) {
        prefsRepository.setAnchorCurrencyCode(currencyCode)
    }

    fun onSwipeToRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.emit(true)
            try {
                refreshRatesUseCase()
            } catch (e: Exception) {
                print(e)
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
