package com.jaaliska.exchangerates.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.Flow
import java.util.*

abstract class BaseHomeViewModel : ViewModel() {

    abstract val anchor: Flow<Item?>
    abstract val items: Flow<List<Item>>
    abstract val updateDate: Flow<Date?>
    abstract val isLoading: Flow<Boolean>
    abstract val error: Flow<Int>
    abstract val isHasFavorites: Flow<Boolean?>

    abstract fun onItemSelection(item: Item)
    abstract fun onAmountChanged(amount: Double)
    abstract fun onSwipeToRefresh()

    data class Item(
        val title: String,
        val subtitle: String,
        val amount: Double
    ) {
        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<Item>() {
                override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                    return oldItem.title == newItem.title
                }

                override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                    return oldItem.title == newItem.title &&
                            oldItem.subtitle == newItem.subtitle &&
                            oldItem.amount == newItem.amount
                }
            }
        }
    }
}