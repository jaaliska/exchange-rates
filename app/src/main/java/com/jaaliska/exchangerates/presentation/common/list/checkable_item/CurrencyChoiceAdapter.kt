package com.jaaliska.exchangerates.presentation.common.list.checkable_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jaaliska.exchangerates.R

class CurrencyChoiceAdapter(
    private val onItemClick: (item: CheckableItem, isCheck: Boolean) -> Unit
) : ListAdapter<CheckableItem, CheckableViewHolder>(CheckableItem.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableViewHolder =
        CheckableViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.currency_choice_item, parent, false)
        )

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: CheckableViewHolder, position: Int) {
        holder.bind(currentList[position], onItemClick)
    }
}