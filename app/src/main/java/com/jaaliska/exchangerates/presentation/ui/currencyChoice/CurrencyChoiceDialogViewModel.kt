package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import com.jaaliska.exchangerates.presentation.model.SelectedCurrency
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CurrencyChoiceDialogViewModel(
    getSupportedCurrenciesUseCase: GetSupportedCurrenciesUseCase,
    private val favoriteCurrenciesUseCase: FavoriteCurrenciesUseCase,
) : ViewModel() {

    val currencies = MutableSharedFlow<List<SelectedCurrency>>(0)
    val errors = MutableSharedFlow<Int>(0)
    val isLoading = MutableStateFlow<Boolean>(true)
    private lateinit var startedFavoritesCodes: List<String>
    private lateinit var modifiedFavoritesCodes: MutableList<String>

    init {
        viewModelScope.launch {
            val supportedCurrencies = getSupportedCurrenciesUseCase(true)
            startedFavoritesCodes = favoriteCurrenciesUseCase.get()
            modifiedFavoritesCodes = mutableListOf(*startedFavoritesCodes.toTypedArray())
            currencies.emit(supportedCurrencies.map {
                SelectedCurrency(
                    name = it.name,
                    code = it.code,
                    isSelected = startedFavoritesCodes.contains(it.code)
                )
            })
            isLoading.emit(false)
        }
    }

    fun onItemClick(currencyCode: String, isChecked: Boolean) {
        if (isChecked) {
            modifiedFavoritesCodes.add(currencyCode)
        } else {
            modifiedFavoritesCodes.remove(currencyCode)
        }
    }

    fun onOkClick(doOnFinish: () -> Unit) {
        viewModelScope.launch {
            isLoading.emit(true)
            if (startedFavoritesCodes == modifiedFavoritesCodes) {
                doOnFinish()
            }
            if (modifiedFavoritesCodes.size > 1) {
                favoriteCurrenciesUseCase.set(modifiedFavoritesCodes)
                doOnFinish()
            } else {
                errors.emit(R.string.not_enough_changed_currency)
            }
            isLoading.emit(false)
        }
    }

    fun onCancelClick(doOnFinish: () -> Unit) {
        viewModelScope.launch {
            isLoading.emit(true)
            if (modifiedFavoritesCodes.size > 1) {
                doOnFinish()
            } else {
                errors.emit(R.string.not_enough_changed_currency)
            }
            isLoading.emit(false)
        }
    }
}