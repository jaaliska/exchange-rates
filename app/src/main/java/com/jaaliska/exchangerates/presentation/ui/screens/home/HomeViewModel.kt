package com.jaaliska.exchangerates.presentation.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.GenericError
import com.jaaliska.exchangerates.domain.model.NetworkError
import com.jaaliska.exchangerates.domain.model.RatesSnapshot
import com.jaaliska.exchangerates.domain.usecase.SetAnchorCurrencyUseCase
import com.jaaliska.exchangerates.presentation.ui.common.list.item.Item
import com.jaaliska.exchangerates.presentation.utils.doOnError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(
    private val ratesDataSource: RatesDataSource,
    private val setAnchorCurrencyUseCase: SetAnchorCurrencyUseCase
) : BaseHomeViewModel() {

    private var rates = MutableStateFlow(listOf<RatesSnapshot.Rate>())
    private var anchorCurrencyAmount = MutableStateFlow(DEFAULT_ANCHOR_CURRENCY_AMOUNT)


    override val items = anchorCurrencyAmount.combine(rates) { anchorAmount, rates ->
        rates.map { rate -> rate.toItem(anchorAmount = anchorAmount) }
    }
    override val anchor = MutableStateFlow<Item?>(null)
    override val updateDate = MutableStateFlow<String?>(null)
    override val isLoading = MutableStateFlow(false)
    override val error = MutableStateFlow<Int?>(null)


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
            .onEach(::emitRatesSnapshot)
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }


    override fun onRefreshItems() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.emit(true)
            ratesDataSource.refresh()
            isLoading.emit(false)
        }
    }

    override fun onItemSelected(item: Item) {
        val selectedCurrency = item.toCurrency()
        viewModelScope.launch(Dispatchers.IO) { setAnchorCurrencyUseCase(selectedCurrency) }
    }

    override fun onAmountChanged(amount: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            anchorCurrencyAmount.emit(amount)
        }
    }

    private suspend fun emitRatesSnapshot(value: RatesSnapshot?) {
        anchor.emit(value?.baseCurrency?.toItem())
        rates.emit(value?.rates ?: listOf())
        updateDate.emit(value?.date?.let(dateFormatter::format))
        anchorCurrencyAmount.emit(DEFAULT_ANCHOR_CURRENCY_AMOUNT)
    }


    /** Mapping */
    private fun Item.toCurrency() = Currency(code = title, name = subtitle)

    private fun RatesSnapshot.Rate.toItem(anchorAmount: Double) = Item(
        title = currency.code,
        subtitle = currency.name,
        value = rate * anchorAmount
    )

    private fun Currency.toItem() = Item(
        title = code,
        subtitle = name,
        value = 0.0
    )

    companion object {
        private val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        private const val DEFAULT_ANCHOR_CURRENCY_AMOUNT = 1.0
    }
}
