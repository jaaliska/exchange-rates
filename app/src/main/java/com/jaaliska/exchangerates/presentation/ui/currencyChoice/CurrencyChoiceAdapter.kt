package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import kotlinx.android.synthetic.main.currency_choice_item.view.*

class CurrencyChoiceAdapter(
    private val onItemClick: (item: BaseCurrencyChoiceViewModel.SelectableItem, isCheck: Boolean) -> Unit
) : ListAdapter<BaseCurrencyChoiceViewModel.SelectableItem, CurrencyChoiceAdapter.CheckableViewHolder>(
    BaseCurrencyChoiceViewModel.SelectableItem.diffCallback
) {

    class CheckableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            item: BaseCurrencyChoiceViewModel.SelectableItem,
            onItemClick: (item: BaseCurrencyChoiceViewModel.SelectableItem, isCheck: Boolean) -> Unit
        ) {
            itemView.apply {
                currencyName.text = item.subtitle
                checkboxCurrency.isChecked = item.isSelected

                setOnClickListener {
                    val changedValue = !checkboxCurrency.isChecked
                    onItemClick(item, changedValue)
                    item.isSelected = changedValue
                    checkboxCurrency.isChecked = changedValue
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