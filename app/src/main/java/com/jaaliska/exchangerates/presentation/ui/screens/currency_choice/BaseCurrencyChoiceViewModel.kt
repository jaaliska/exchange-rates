package com.jaaliska.exchangerates.presentation.ui.screens.currency_choice

import androidx.lifecycle.ViewModel
import com.jaaliska.exchangerates.presentation.ui.common.list.checkable_item.CheckableItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseCurrencyChoiceViewModel : ViewModel() {

    protected val _state = MutableStateFlow(State())
    val state: Flow<State> = _state

    data class State(
        val items: List<CheckableItem> = listOf(),
        val error: Int? = null,
        val isLoading: Boolean = false
    )

    abstract fun onItemClick(item: CheckableItem, isChecked: Boolean)


    protected suspend fun submitItems(items: List<CheckableItem>) {
        _state.emit(_state.value.copy(items = items))
    }

    protected suspend fun submitError(error: Int?) {
        _state.emit(_state.value.copy(error = error))
    }

    protected suspend fun submitLoading(isLoading: Boolean) {
        _state.emit(_state.value.copy(isLoading = isLoading))
    }
}