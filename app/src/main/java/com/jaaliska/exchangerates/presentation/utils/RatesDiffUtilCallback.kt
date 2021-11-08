package com.jaaliska.exchangerates.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.jaaliska.exchangerates.domain.model.Rate

class RatesDiffUtilCallback : DiffUtil.ItemCallback<Rate>() {

    override fun areItemsTheSame(oldItem: Rate, newItem: Rate): Boolean {
        return oldItem.currency == oldItem.currency &&
                oldItem.rate == newItem.rate
    }

    override fun areContentsTheSame(
        oldItem: Rate,
        newItem: Rate
    ): Boolean {
        return oldItem == newItem
    }
}
