package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.Flow

abstract class BaseCurrencyChoiceViewModel : ViewModel() {

    abstract val items: Flow<List<CheckableItem>>
    abstract val error: Flow<Int?>
    abstract val isLoading: Flow<Boolean>

    abstract fun onItemClick(item: CheckableItem, isChecked: Boolean)


    data class CheckableItem(
        val title: String,
        val subtitle: String,
        var isChecked: Boolean
    ) {
        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<CheckableItem>() {
                override fun areItemsTheSame(
                    oldItem: CheckableItem,
                    newItem: CheckableItem
                ): Boolean {
                    return oldItem.title == newItem.title &&
                            oldItem.subtitle == newItem.subtitle &&
                            oldItem.isChecked == newItem.isChecked
                }

                override fun areContentsTheSame(
                    oldItem: CheckableItem,
                    newItem: CheckableItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}