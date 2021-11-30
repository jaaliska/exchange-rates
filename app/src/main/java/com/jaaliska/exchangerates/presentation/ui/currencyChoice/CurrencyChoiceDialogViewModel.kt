package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import com.jaaliska.exchangerates.presentation.common.error.ErrorHandler
import com.jaaliska.exchangerates.presentation.common.list.checkable_item.CheckableItem
import com.jaaliska.exchangerates.presentation.common.mapping.toCurrency
import com.jaaliska.exchangerates.presentation.common.mapping.toSelectableItem
import com.jaaliska.exchangerates.presentation.common.utils.safeLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CurrencyChoiceDialogViewModel(
    getSupportedCurrenciesUseCase: GetSupportedCurrenciesUseCase,
    private val favoriteCurrenciesUseCase: FavoriteCurrenciesUseCase,
) : BaseCurrencyChoiceDialogViewModel() {

    override val items = MutableStateFlow(listOf<CheckableItem>())
    override val error = MutableSharedFlow<Int?>(0)
    override val isLoading = MutableStateFlow(false)

    private val errorHandler = ErrorHandler()

    init {
        viewModelScope.safeLaunch(handleError = ::handleError) {
            isLoading.emit(true)

            val supportedCurrencies = getSupportedCurrenciesUseCase(true)
            val favorites = favoriteCurrenciesUseCase.get()

            items.emit(supportedCurrencies.map {
                it.toSelectableItem(favorites.contains(it))
            })
            isLoading.emit(false)
        }
    }

    override fun onItemSelected(item: CheckableItem, isChecked: Boolean) {
        val newItems = items.value.toMutableList()
        val index = newItems.indexOf(item)
        newItems[index] = item.copy(isSelected = isChecked)

        viewModelScope.launch { items.emit(newItems) }
    }

    override fun submit(onSuccess: () -> Unit) {
        viewModelScope.safeLaunch(Dispatchers.IO, handleError = ::handleError) {
            isLoading.emit(true)

            val newFavorites = items.value
                .filter { it.isSelected }
                .map { it.toCurrency() }
            favoriteCurrenciesUseCase.set(newFavorites)

            isLoading.emit(false)
            onSuccess()
        }
    }


    private suspend fun handleError(e: Exception) {
        val errorRes = errorHandler.map(e)

        error.emit(errorRes)
        isLoading.emit(false)
    }
}