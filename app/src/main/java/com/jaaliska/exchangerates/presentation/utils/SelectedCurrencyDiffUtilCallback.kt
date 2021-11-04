package com.jaaliska.exchangerates.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.jaaliska.exchangerates.presentation.model.SelectedCurrency

class SelectedCurrencyDiffUtilCallback : DiffUtil.ItemCallback<SelectedCurrency>() {
    override fun areItemsTheSame(oldItem: SelectedCurrency, newItem: SelectedCurrency): Boolean {
        return oldItem.code == newItem.code &&
                oldItem.name == newItem.name &&
                oldItem.isSelected == newItem.isSelected
    }

    override fun areContentsTheSame(oldItem: SelectedCurrency, newItem: SelectedCurrency): Boolean {
        return oldItem == newItem
    }
}