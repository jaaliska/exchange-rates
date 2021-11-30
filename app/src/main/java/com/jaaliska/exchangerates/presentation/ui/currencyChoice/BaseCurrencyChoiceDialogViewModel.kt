package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.ViewModel
import com.jaaliska.exchangerates.presentation.common.list.checkable_item.CheckableItem
import kotlinx.coroutines.flow.Flow

abstract class BaseCurrencyChoiceDialogViewModel : ViewModel() {

    // output
    abstract val items: Flow<List<CheckableItem>>
    abstract val error: Flow<Int?>
    abstract val isLoading: Flow<Boolean>

    // input
    abstract fun onItemSelected(item: CheckableItem, isChecked: Boolean)
    abstract fun submit(onSuccess: () -> Unit)
}