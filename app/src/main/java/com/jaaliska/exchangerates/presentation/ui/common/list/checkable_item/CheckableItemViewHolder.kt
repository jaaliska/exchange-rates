package com.jaaliska.exchangerates.presentation.ui.common.list.checkable_item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_checkable.view.*

class CheckableItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        item: CheckableItem,
        onItemClick: (item: CheckableItem, isCheck: Boolean) -> Unit
    ) {
        itemView.apply {
            textTitle.text = item.title
            textSubtitle.text = item.subtitle
            checkbox.isChecked = item.isChecked

            setOnClickListener {
                onItemClick(item, !checkbox.isChecked)
            }
        }
    }
}