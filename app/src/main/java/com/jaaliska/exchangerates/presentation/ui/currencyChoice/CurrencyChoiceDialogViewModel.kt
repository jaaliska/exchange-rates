package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.domain.repository.CurrenciesRepository
import com.jaaliska.exchangerates.domain.usecases.SetFavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.presentation.error.ErrorHandler
import com.jaaliska.exchangerates.presentation.mapping.toCurrency
import com.jaaliska.exchangerates.presentation.mapping.toSelectableItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CurrencyChoiceDialogViewModel(
    private val setFavoriteCurrencies: SetFavoriteCurrenciesUseCase,
    private val currencies: CurrenciesRepository,
) : BaseCurrencyChoiceDialogViewModel() {

    override val items = MutableStateFlow<List<SelectableItem>>(listOf())
    override val error = MutableSharedFlow<Int>(0)
    override val isLoading = MutableStateFlow(false)
    private val errorHandler = ErrorHandler()

    init {
        viewModelScope.launch {
            isLoading.emit(true)
            try {
                val supportedCurrencies = currencies.getSupported(true)
                val favorites = currencies.getFavorite()
                items.emit(supportedCurrencies.map { it.toSelectableItem(favorites.contains(it)) })
            } catch (ex: Exception) {
                error.emit(errorHandler.map(ex))
            }
            isLoading.emit(false)
        }
    }

    override fun onItemSelected(item: SelectableItem, isChecked: Boolean) {
        val newItems = items.value.toMutableList()
        val index = newItems.indexOf(item)
        newItems[index] = item.copy(isSelected = isChecked)

        viewModelScope.launch { items.emit(newItems) }
    }

    override fun submitItems(doOnFinish: () -> Unit) {
        viewModelScope.launch {
            isLoading.emit(true)
            val newFavorites = items.value
                .filter { it.isSelected }
                .map { it.toCurrency() }
            try {
                setFavoriteCurrencies.invoke(newFavorites.map { it.code }.toSet())
            } catch (ex: Exception) {
                error.emit(errorHandler.map(ex))
                isLoading.emit(false)
                return@launch
            }
            isLoading.emit(false)
            doOnFinish()
        }
    }
}