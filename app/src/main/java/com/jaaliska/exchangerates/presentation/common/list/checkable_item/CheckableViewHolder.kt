package com.jaaliska.exchangerates.presentation.common.list.checkable_item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.currency_choice_item.view.*

class CheckableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: CheckableItem,
            onItemClick: (item: CheckableItem, isCheck: Boolean) -> Unit
        ) {
            itemView.apply {
                title.text = item.title
                subtitle.text = item.subtitle
                checkbox.isChecked = item.isSelected

                setOnClickListener {
                    onItemClick(item, !item.isSelected)
                }
            }
        }
    }