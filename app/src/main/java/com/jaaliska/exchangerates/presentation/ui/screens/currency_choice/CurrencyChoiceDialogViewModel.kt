package com.jaaliska.exchangerates.presentation.ui.screens.currency_choice

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecase.UpdateCurrencyFavoriteStateUseCase
import com.jaaliska.exchangerates.presentation.ui.common.list.checkable_item.CheckableItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CurrencyChoiceDialogViewModel(
    currenciesDataSource: CurrenciesDataSource,
    private val updateCurrencyFavoriteStateUseCase: UpdateCurrencyFavoriteStateUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseCurrencyChoiceViewModel() {

    init {
        currenciesDataSource.observeFavorites()
            .combine(currenciesDataSource.observeAll()) { favorites, currencies ->
                currencies.map { currency ->
                    val isChecked = favorites.any { it.code == currency.code }
                    currency.toCheckableItem(isChecked)
                }
            }
            .onEach(::submitItems)
            .flowOn(ioDispatcher)
            .catch { submitError(R.string.something_went_wrong) }
            .launchIn(viewModelScope)
    }

    override fun onItemClick(item: CheckableItem, isChecked: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            submitLoading(true)
            updateCurrencyFavoriteStateUseCase(currency = item.toCurrency(), isFavorite = isChecked)
            submitLoading(false)
        }
    }

    companion object {
        internal fun CheckableItem.toCurrency() = Currency(code = title, name = subtitle)
        internal fun Currency.toCheckableItem(isChecked: Boolean) = CheckableItem(
            title = code,
            subtitle = name,
            isChecked = isChecked
        )
    }
}