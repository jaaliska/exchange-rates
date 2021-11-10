package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import kotlinx.android.synthetic.main.list_item_checkable.view.*

class CurrencyChoiceAdapter(
    private val onItemClick: (item: BaseCurrencyChoiceViewModel.CheckableItem, isCheck: Boolean) -> Unit
) : ListAdapter<BaseCurrencyChoiceViewModel.CheckableItem, CurrencyChoiceAdapter.CheckableViewHolder>(
    BaseCurrencyChoiceViewModel.CheckableItem.diffCallback
) {

    class CheckableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            item: BaseCurrencyChoiceViewModel.CheckableItem,
            onItemClick: (item: BaseCurrencyChoiceViewModel.CheckableItem, isCheck: Boolean) -> Unit
        ) {
            itemView.apply {
                title.text = item.title
                subtitle.text = item.subtitle
                checkbox.isChecked = item.isChecked

                setOnClickListener {
                    onItemClick(item, !checkbox.isChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableViewHolder =
        CheckableViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_checkable, parent, false)
        )

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: CheckableViewHolder, position: Int) {
        holder.bind(currentList[position], onItemClick)
    }

}