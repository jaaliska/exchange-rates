package com.jaaliska.exchangerates.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.data.rates.repository.BaseCurrencyCode
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.Rate
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.presentation.error.ErrorHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    private val ratesDataSource: RatesDataSource,
    private val prefsRepository: PreferencesRepository,
    private val currencies: CurrenciesDataSource,
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
    override val error = MutableSharedFlow<Int>(0)
    override val isHasFavorites = MutableStateFlow<Boolean?>(null)

    private val errorHandler = ErrorHandler()

    init {
        viewModelScope.launch {
            val favorites = currencies.getFavorite()
            if (favorites.isNotEmpty()) {
                updateExchangeRates(prefsRepository.getBaseCurrencyCode())
            } else {
                isHasFavorites.emit(false)
            }
            getRatesUpdateDates
                .catch {
                    error.emit(errorHandler.map(it))
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
                ratesDataSource.refresh()
            } catch (ex: Exception) {
                error.emit(errorHandler.map(ex))
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
                ratesDataSource.getNamedRates(baseCurrencyCode).apply {
                    prefsRepository.setBaseCurrencyCode(this.baseCurrency.code)
                    applyExchangeRatesToScreen(this)
                    if (onFinished != null) {
                        onFinished()
                    }
                }
            } catch (ex: Exception) {
                error.emit(errorHandler.map(ex))
            } finally {
                isHasFavorites.emit(true)
                isLoading.emit(false)
            }
        }
    }

    private suspend fun applyExchangeRatesToScreen(value: ExchangeRates) {
        rates.emit(value.rates)
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
