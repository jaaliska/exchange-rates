package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.BaseCurrencyChoiceDialogViewModel.SelectableItem
import kotlinx.android.synthetic.main.currency_choice_item.view.*

class CurrencyChoiceAdapter(
    private val onItemClick: (item: SelectableItem, isCheck: Boolean) -> Unit
) : ListAdapter<SelectableItem, CurrencyChoiceAdapter.CheckableViewHolder>(
    SelectableItem.diffCallback
) {

    class CheckableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: SelectableItem,
            onItemClick: (item: SelectableItem, isCheck: Boolean) -> Unit
        ) {
            itemView.apply {
                title.text = item.title
                subtitle.text = item.subtitle
                checkbox.isChecked = item.isSelected

                this.setOnClickListener {
                    onItemClick(item, !item.isSelected)
                }
            }
        }
    }

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