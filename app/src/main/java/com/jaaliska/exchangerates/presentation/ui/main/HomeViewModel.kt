package com.jaaliska.exchangerates.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.data.rates.repository.BaseCurrencyCode
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetNamedRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import com.jaaliska.exchangerates.presentation.error.ErrorHandler
import com.jaaliska.exchangerates.presentation.model.NamedExchangeRates
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog
import com.jaaliska.exchangerates.presentation.utils.doOnError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val getNamedRatesUseCase: GetNamedRatesUseCase,
    private val refreshRatesUseCase: RefreshRatesUseCase,
    private val prefsRepository: PreferencesRepository,
    private val favoriteCurrenciesUseCase: FavoriteCurrenciesUseCase,
    private val getRatesUpdateDates: Flow<Map<BaseCurrencyCode, Date>>
) : BaseHomeViewModel() {

    override val exchangeRates = MutableStateFlow<List<NamedRate>>(listOf())
    override val baseCurrencyAmount = MutableStateFlow<Double>(DEFAULT_BASE_CURRENCY_AMOUNT)
    override val baseCurrencyDetails = MutableStateFlow<Currency?>(null)
    override val updateDate = MutableStateFlow<Date?>(null)
    override val isLoading = MutableStateFlow<Boolean>(false)
    override val errors = MutableSharedFlow<Int>(0)
    override val currencyChoiceDialog = MutableSharedFlow<CurrencyChoiceDialog>(0)
    private val errorHandler = ErrorHandler()

    init {
        viewModelScope.launch {
            val favorites = favoriteCurrenciesUseCase.get()
            if (favorites.isNotEmpty()) {
                updateExchangeRates(prefsRepository.getBaseCurrencyCode())
            } else {
                showCurrencyChoiceDialog()
            }
            getRatesUpdateDates
                .doOnError {
                    errors.emit(errorHandler.map(it))
                }
                .onEach {
                    val baseCurrencyCode = prefsRepository.getBaseCurrencyCode()
                    val date: Date? = it[baseCurrencyCode]
                    if (date != null && (updateDate.value == null || date > updateDate.value)) {
                        updateExchangeRates(baseCurrencyCode)
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun showCurrencyChoiceDialog() {
        viewModelScope.launch {
            val dialog = CurrencyChoiceDialog()
            currencyChoiceDialog.emit(dialog)
        }
    }

    override fun onCurrencySelection(currencyCode: String, amount: Double) {
        updateExchangeRates(currencyCode) {
            baseCurrencyAmount.value = amount
        }
    }

    override fun onSwipeToRefresh() {
        viewModelScope.launch {
            isLoading.emit(true)
            try {
                refreshRatesUseCase()
            } catch (ex: Exception) {
                errors.emit(errorHandler.map(ex))
            } finally {
                isLoading.emit(false)
            }
        }
    }

    private fun updateExchangeRates(
        baseCurrencyCode: String,
        onFinished: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            isLoading.emit(true)
            try {
                getNamedRatesUseCase(baseCurrencyCode).apply {
                    prefsRepository.setBaseCurrencyCode(this.baseCurrency.code)
                    applyExchangeRatesToScreen(this)
                    if (onFinished != null) {
                        onFinished()
                    }
                }
            } catch (ex: Exception) {
                errors.emit(errorHandler.map(ex))
            } finally {
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
