package com.jaaliska.exchangerates.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

abstract class BaseHomeViewModel : ViewModel() {

    abstract val items: Flow<List<Item>>
    abstract val baseCurrencyAmount: MutableStateFlow<Double>
    abstract val baseCurrencyDetails: Flow<Currency?>
    abstract val updateDate: Flow<Date?>
    abstract val isLoading: Flow<Boolean>
    abstract val errors: Flow<Int>
    abstract val currencyChoiceDialog: Flow<CurrencyChoiceDialog>

    abstract fun onCurrencySelection(currencyCode: String, amount: Double)
    abstract fun onSwipeToRefresh()

    data class Item(
        val title: String,
        val subtitle: String,
        val amount: Double
    ) {
        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<Item>() {
                override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                    return oldItem.title == newItem.title &&
                            oldItem.subtitle == newItem.subtitle &&
                            oldItem.amount == newItem.amount
                }

                override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}