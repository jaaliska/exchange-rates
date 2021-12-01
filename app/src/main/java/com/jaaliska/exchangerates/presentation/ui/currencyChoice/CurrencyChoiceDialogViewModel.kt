package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import com.jaaliska.exchangerates.presentation.error.ErrorHandler
import com.jaaliska.exchangerates.presentation.mapping.toCurrency
import com.jaaliska.exchangerates.presentation.mapping.toSelectableItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CurrencyChoiceDialogViewModel(
    getSupportedCurrenciesUseCase: GetSupportedCurrenciesUseCase,
    private val favoriteCurrenciesUseCase: FavoriteCurrenciesUseCase,
) : BaseCurrencyChoiceDialogViewModel() {

    override val items = MutableStateFlow<List<SelectableItem>>(listOf())
    override val errors = MutableSharedFlow<Int>(0)
    override val isLoading = MutableStateFlow(false)
    private val errorHandler = ErrorHandler()

    init {
        viewModelScope.launch {
            isLoading.emit(true)
            try {
                val supportedCurrencies = getSupportedCurrenciesUseCase(true)
                val favorites = favoriteCurrenciesUseCase.get()
                items.emit(supportedCurrencies.map { it.toSelectableItem(favorites.contains(it)) })
            } catch (ex: Exception) {
                errors.emit(errorHandler.map(ex))
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

    override fun onOkClick(doOnFinish: () -> Unit) {
        viewModelScope.launch {
            isLoading.emit(true)
            val newFavorites = items.value
                .filter { it.isSelected }
                .map { it.toCurrency() }
            try {
                favoriteCurrenciesUseCase.set(newFavorites.map { it.code }.toSet())
            } catch (ex: Exception) {
                errors.emit(errorHandler.map(ex))
                isLoading.emit(false)
                return@launch
            }
            isLoading.emit(false)
            doOnFinish()
        }
    }
}