package com.jaaliska.exchangerates.presentation.common.list.checkable_item

import androidx.recyclerview.widget.DiffUtil

data class CheckableItem(
        val title: String,
        val subtitle: String,
        val isSelected: Boolean
    ) {
        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<CheckableItem>() {
                override fun areItemsTheSame(
                    oldItem: CheckableItem,
                    newItem: CheckableItem
                ): Boolean {
                    return oldItem.title == newItem.title &&
                            oldItem.subtitle == newItem.subtitle &&
                            oldItem.isSelected == newItem.isSelected
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