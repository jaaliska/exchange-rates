package com.jaaliska.exchangerates.presentation.ui.common.list.checkable_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jaaliska.exchangerates.R

// fast adapter
class CheckableItemAdapter(
    private val onItemClick: (item: CheckableItem, isCheck: Boolean) -> Unit
) : ListAdapter<CheckableItem, CheckableItemViewHolder>(
    CheckableItem.diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableItemViewHolder =
        CheckableItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_checkable, parent, false)
        )

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: CheckableItemViewHolder, position: Int) {
        holder.bind(currentList[position], onItemClick)
    }

}