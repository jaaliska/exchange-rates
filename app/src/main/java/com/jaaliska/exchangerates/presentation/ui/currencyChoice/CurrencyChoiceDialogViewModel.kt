package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecase.UpdateCurrencyFavoriteStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CurrencyChoiceDialogViewModel(
    currenciesDataSource: CurrenciesDataSource,
    private val updateCurrencyFavoriteStateUseCase: UpdateCurrencyFavoriteStateUseCase,
) : BaseCurrencyChoiceViewModel() {

    override val items = MutableStateFlow<List<CheckableItem>>(listOf())
    override val error = MutableStateFlow<Int?>(null)
    override val isLoading = MutableStateFlow(false)

    init {
        currenciesDataSource.observeFavorites()
            .zip(currenciesDataSource.observeAll()) { favorites, currencies ->
                currencies.map { currency ->
                    val isChecked = favorites.any { it.code == currency.code }
                    currency.toCheckableItem(isChecked)
                }
            }.onEach(items::emit)
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    override fun onItemClick(item: CheckableItem, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCurrencyFavoriteStateUseCase(currency = item.toCurrency(), isFavorite = isChecked)
        }
    }


    private fun CheckableItem.toCurrency() = Currency(code = title, name = subtitle)
    private fun Currency.toCheckableItem(isChecked: Boolean) = CheckableItem(
        title = code,
        subtitle = name,
        isChecked = isChecked
    )
}