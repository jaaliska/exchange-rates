package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import com.jaaliska.exchangerates.presentation.model.SelectedCurrency
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CurrencyChoiceDialogViewModel(
    getSupportedCurrenciesUseCase: GetSupportedCurrenciesUseCase,
    private val favoriteCurrenciesUseCase: FavoriteCurrenciesUseCase,
) : ViewModel() {

    val currencies = MutableSharedFlow<List<SelectedCurrency>>(0)
    private lateinit var modifiedFavoritesCodes: MutableList<String>

    init {
        viewModelScope.launch {
            val supportedCurrencies = getSupportedCurrenciesUseCase(true)
            val favoriteCurrencies = favoriteCurrenciesUseCase.get()
            modifiedFavoritesCodes = mutableListOf(*favoriteCurrencies.toTypedArray())
            currencies.emit(supportedCurrencies.map {
                SelectedCurrency(
                    name = it.name,
                    code = it.code,
                    isSelected = favoriteCurrencies.contains(it.code)
                )
            })
        }
    }

    fun onItemClick(currencyCode: String, isChecked: Boolean) {
        if (isChecked) {
            modifiedFavoritesCodes.add(currencyCode)
        } else {
            modifiedFavoritesCodes.remove(currencyCode)
        }
    }

    fun onOkClick() {
        viewModelScope.launch {
            favoriteCurrenciesUseCase.set(modifiedFavoritesCodes)
        }
    }

}