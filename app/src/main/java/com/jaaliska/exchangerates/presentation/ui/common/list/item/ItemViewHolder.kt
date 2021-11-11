package com.jaaliska.exchangerates.presentation.ui.common.list.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_home.view.*

class ItemViewHolder(
    itemView: View,
    private val onItemClick: (item: Item) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Item) {
            itemView.apply {
                textTitle.text = item.title
                textSubtitle.text = item.subtitle
                textValue.text = String.format(TEXT_VALUE_FORMAT, item.value)

                setOnClickListener { onItemClick(item) }
            }
        }

        companion object {
            private const val TEXT_VALUE_FORMAT = "%.2f"
        }
    }