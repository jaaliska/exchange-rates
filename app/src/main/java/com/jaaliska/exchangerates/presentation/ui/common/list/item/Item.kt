package com.jaaliska.exchangerates.presentation.ui.common.list.item

import androidx.recyclerview.widget.DiffUtil

data class Item(val title: String, val subtitle: String, val value: Double) {

    companion object {
        val diffUtilCallback = object : DiffUtil.ItemCallback<Item>() {

            override fun areItemsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem.title == oldItem.title &&
                        oldItem.subtitle == newItem.subtitle &&
                        oldItem.value == newItem.value
            }

            override fun areContentsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}