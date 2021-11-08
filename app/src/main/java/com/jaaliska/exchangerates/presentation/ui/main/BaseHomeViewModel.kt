package com.jaaliska.exchangerates.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

abstract class BaseHomeViewModel : ViewModel() {

    abstract val exchangeRates: Flow<List<NamedRate>>
    abstract val baseCurrencyAmount: MutableStateFlow<Double>
    abstract val baseCurrencyDetails: Flow<Currency?>
    abstract val updateDate: Flow<Date?>
    abstract val isLoading: Flow<Boolean>
    abstract val errors: Flow<Int>
    abstract val currencyChoiceDialog: Flow<CurrencyChoiceDialog>

    abstract fun onCurrencySelection(currencyCode: String, amount: Double)
    abstract fun onSwipeToRefresh()

    data class NamedRate(
        val currencyCode: String,
        val currencyName: String,
        val exchangeRate: Double
    ) {
        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<NamedRate>() {
                override fun areItemsTheSame(oldItem: NamedRate, newItem: NamedRate): Boolean {
                    return oldItem.currencyCode == newItem.currencyCode &&
                            oldItem.currencyName == newItem.currencyName &&
                            oldItem.exchangeRate == newItem.exchangeRate
                }

                override fun areContentsTheSame(oldItem: NamedRate, newItem: NamedRate): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}