package com.jaaliska.exchangerates.presentation.ui.screens.currencyChoice

import androidx.lifecycle.ViewModel
import com.jaaliska.exchangerates.presentation.ui.common.list.checkable_item.CheckableItem
import kotlinx.coroutines.flow.Flow

abstract class BaseCurrencyChoiceViewModel : ViewModel() {

    abstract val items: Flow<List<CheckableItem>>
    abstract val error: Flow<Int?>
    abstract val isLoading: Flow<Boolean>

    abstract fun onItemClick(item: CheckableItem, isChecked: Boolean)
}