package com.jaaliska.exchangerates.presentation.ui.common.list.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.common.list.item.Item.Companion.diffUtilCallback

class ItemAdapter(private val onItemClick: (item: Item) -> Unit) :
    ListAdapter<Item, ItemViewHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_home, parent, false),
            onItemClick
        )
    }

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}