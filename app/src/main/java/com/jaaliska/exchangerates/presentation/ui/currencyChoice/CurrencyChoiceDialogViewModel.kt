package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import com.jaaliska.exchangerates.presentation.error.ErrorHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CurrencyChoiceDialogViewModel(
    getSupportedCurrenciesUseCase: GetSupportedCurrenciesUseCase,
    private val favoriteCurrenciesUseCase: FavoriteCurrenciesUseCase,
) : BaseCurrencyChoiceDialogViewModel() {

    override val items = MutableSharedFlow<List<SelectableItem>>(0)
    override val errors = MutableSharedFlow<Int>(0)
    override val isLoading = MutableStateFlow<Boolean>(true)
    private val errorHandler = ErrorHandler()
    private lateinit var startedFavoritesCodes: List<String>
    private lateinit var modifiedFavoritesCodes: MutableList<String>

    init {
        viewModelScope.launch {
            try {
                val supportedCurrencies = getSupportedCurrenciesUseCase(true)
                startedFavoritesCodes = favoriteCurrenciesUseCase.get().map { it.code }
                modifiedFavoritesCodes = mutableListOf(*startedFavoritesCodes.toTypedArray())
                items.emit(supportedCurrencies.map {
                    SelectableItem(
                        subtitle = it.name,
                        title = it.code,
                        isSelected = startedFavoritesCodes.contains(it.code)
                    )
                })
            } catch (ex: Exception) {
                errors.emit(errorHandler.map(ex))
            }

            isLoading.emit(false)
        }
    }

    override fun onItemClick(currencyCode: String, isChecked: Boolean) {
        if (isChecked) {
            modifiedFavoritesCodes.add(currencyCode)
        } else {
            modifiedFavoritesCodes.remove(currencyCode)
        }
    }

    override fun onOkClick(doOnFinish: () -> Unit) {
        viewModelScope.launch {
            isLoading.emit(true)
            if (startedFavoritesCodes == modifiedFavoritesCodes) {
                doOnFinish()
            }
            if (modifiedFavoritesCodes.size > 1) {
                try {
                    favoriteCurrenciesUseCase.set(modifiedFavoritesCodes)
                } catch (ex: java.lang.Exception) {
                    errors.emit(errorHandler.map(ex))
                }
                doOnFinish()
            } else {
                errors.emit(R.string.not_enough_changed_currency)
            }
            isLoading.emit(false)
        }
    }

    override fun onCancelClick(doOnFinish: () -> Unit) {
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