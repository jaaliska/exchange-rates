package com.jaaliska.exchangerates.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.jaaliska.exchangerates.domain.model.RatesSnapshot

class RatesDiffUtilCallback : DiffUtil.ItemCallback<RatesSnapshot.Rate>() {

    override fun areItemsTheSame(oldItem: RatesSnapshot.Rate, newItem: RatesSnapshot.Rate): Boolean {
        return oldItem.currency == oldItem.currency &&
                oldItem.rate == newItem.rate
    }

    override fun areContentsTheSame(
        oldItem: RatesSnapshot.Rate,
        newItem: RatesSnapshot.Rate
    ): Boolean {
        return oldItem == newItem
    }
}
