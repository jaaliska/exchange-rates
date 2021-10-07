package com.jaaliska.exchangerates.presentation.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaaliska.exchangerates.domain.model.ResultWrapper
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: CurrencyRepository
) : ViewModel() {

    fun init() {
        viewModelScope.launch {
            val supportedCurrencies = repository.getExchangeRates("AFN")
            when (supportedCurrencies) {
                is ResultWrapper.NetworkError -> Log.d("HomeViewModel", supportedCurrencies.toString())
                is ResultWrapper.GenericError -> Log.d("HomeViewModel", supportedCurrencies.toString())
                is ResultWrapper.Success<*> -> Log.d("HomeViewModel", supportedCurrencies.toString())
            }
        }
    }
}