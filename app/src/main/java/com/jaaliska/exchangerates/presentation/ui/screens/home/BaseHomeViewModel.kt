package com.jaaliska.exchangerates.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import com.jaaliska.exchangerates.presentation.ui.common.list.item.Item
import kotlinx.coroutines.flow.Flow

abstract class BaseHomeViewModel : ViewModel() {
    abstract val anchor: Flow<Item?>
    abstract val items: Flow<List<Item>>
    abstract val updateDate: Flow<String?>

    abstract val isLoading: Flow<Boolean>
    abstract val error: Flow<Int?>

    abstract fun onRefreshItems()
    abstract fun onItemSelected(item: Item)
    abstract fun onAmountChanged(amount: Double)
}