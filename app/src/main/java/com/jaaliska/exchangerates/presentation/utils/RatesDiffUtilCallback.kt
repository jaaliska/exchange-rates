package com.jaaliska.exchangerates.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.jaaliska.exchangerates.presentation.model.NamedRate

class RatesDiffUtilCallback : DiffUtil.ItemCallback<NamedRate>() {

    override fun areItemsTheSame(oldItem: NamedRate, newItem: NamedRate): Boolean {
        return oldItem.currencyCode == newItem.currencyCode &&
                oldItem.currencyName == newItem.currencyName &&
                oldItem.exchangeRate == newItem.exchangeRate
    }

    override fun areContentsTheSame(
        oldItem: NamedRate,
        newItem: NamedRate
    ): Boolean {
        return oldItem == newItem
    }
}
