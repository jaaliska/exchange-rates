package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import kotlinx.android.synthetic.main.currency_choice_item.view.*

class CurrencyChoiceAdapter(
    private val onItemClick: (currencyCode: String, isCheck: Boolean) -> Unit
) : ListAdapter<BaseCurrencyChoiceDialogViewModel.SelectableItem, CurrencyChoiceAdapter.CheckableViewHolder>(
    BaseCurrencyChoiceDialogViewModel.SelectableItem.diffCallback
) {

    class CheckableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            items: BaseCurrencyChoiceDialogViewModel.SelectableItem,
            onItemClick: (currencyCode: String, isCheck: Boolean) -> Unit
        ) {
            itemView.apply {
                title.text = items.title
                subtitle.text = items.subtitle
                checkbox.isChecked = items.isSelected
                this.setOnClickListener {
                    val changedValue = !checkbox.isChecked
                    onItemClick(items.title, changedValue)
                    items.isSelected = changedValue
                    checkbox.isChecked = changedValue
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