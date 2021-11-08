package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CurrencyChoiceDialogViewModel(
    observeAllCurrencies: GetSupportedCurrenciesUseCase,
    private val observeFavorites: FavoriteCurrenciesUseCase,
) : BaseCurrencyChoiceViewModel() {

    override val items = MutableStateFlow<List<SelectableItem>>(listOf())
    override val error = MutableStateFlow<Int?>(null)
    override val isLoading = MutableStateFlow(false)

    init {
        observeFavorites.get().zip(observeAllCurrencies()) { favorites, currencies ->
            currencies.map { currency ->
                SelectableItem(
                    title = currency.code,
                    subtitle = currency.name,
                    isSelected = favorites.any { it.code == currency.code })
            }
        }.onEach(items::emit).flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    override fun onItemClick(item: SelectableItem, isChecked: Boolean) {
        viewModelScope.launch {
            observeFavorites.set(code = item.title, isFavorite = isChecked)
        }
    }
}