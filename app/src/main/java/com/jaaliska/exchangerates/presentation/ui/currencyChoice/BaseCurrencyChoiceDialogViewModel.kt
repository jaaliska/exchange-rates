package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseCurrencyChoiceDialogViewModel: ViewModel() {

    abstract val items: Flow<List<SelectableItem>>
    abstract val errors: Flow<Int>
    abstract val isLoading: StateFlow<Boolean>

    abstract fun onItemClick(currencyCode: String, isChecked: Boolean)
    abstract fun onOkClick()
    abstract fun onCancelClick()

    data class SelectableItem(
        val title: String,
        val subtitle: String,
        var isSelected: Boolean
    ) {
        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<SelectableItem>() {
                override fun areItemsTheSame(
                    oldItem: SelectableItem,
                    newItem: SelectableItem
                ): Boolean {
                    return oldItem.title == newItem.title &&
                            oldItem.subtitle == newItem.subtitle &&
                            oldItem.isSelected == newItem.isSelected
                }

                override fun areContentsTheSame(
                    oldItem: SelectableItem,
                    newItem: SelectableItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}