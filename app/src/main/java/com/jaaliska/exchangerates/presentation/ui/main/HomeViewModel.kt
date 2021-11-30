package com.jaaliska.exchangerates.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.data.rates.repository.BaseCurrencyCode
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetNamedRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import com.jaaliska.exchangerates.presentation.common.error.ErrorHandler
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog
import com.jaaliska.exchangerates.presentation.common.utils.doOnError
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

    private var rates = MutableStateFlow(listOf<Rate>())
    private val anchorCurrencyAmount = MutableStateFlow<Double>(DEFAULT_BASE_CURRENCY_AMOUNT)
    override val items = anchorCurrencyAmount.combine(rates) { anchorAmount, rates ->
        rates.map { rate -> rate.toItem(anchorAmount = anchorAmount) }
    }
    override val anchor = MutableStateFlow<Item?>(null)
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

    override fun onItemSelection(item: Item) {
        updateExchangeRates(item.title)
    }

    override fun onAmountChanged(amount: Double) {
        viewModelScope.launch {
            anchorCurrencyAmount.emit(amount)
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

    private suspend fun applyExchangeRatesToScreen(value: ExchangeRates) {
        rates.emit( value.rates)
        anchor.emit(value.baseCurrency.toItem())
        updateDate.value = value.date
    }

    private fun Rate.toItem(anchorAmount: Double) = Item(
        title = currency.code,
        subtitle = currency.name,
        amount = exchangeRate * anchorAmount
    )
    private fun Currency.toItem() = Item(
        title = code,
        subtitle = name,
        amount = anchorCurrencyAmount.value
    )

    companion object {
        private const val DEFAULT_BASE_CURRENCY_AMOUNT = 1.0
    }
}
