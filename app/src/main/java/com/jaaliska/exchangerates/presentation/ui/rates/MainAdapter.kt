package com.jaaliska.exchangerates.presentation.ui.rates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.rates.BaseRatesViewModel.Item
import kotlinx.android.synthetic.main.exchange_rates_item.view.*

class MainAdapter(
    private val onItemClick: (item: Item) -> Unit
) : ListAdapter<Item, MainAdapter.DataViewHolder>(Item.diffCallback) {

    class DataViewHolder(
        itemView: View,
        private val onItemClick: (item: Item) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            item: Item
        ) {
            itemView.apply {
                title.text = item.title
                subtitle.text = item.subtitle
                tvResultAmount.text = String.format(TEXT_VALUE_FORMAT, item.amount)
                this.setOnClickListener { onItemClick(item) }
            }
        }

        fun bindAmount(amount: Double) {
            itemView.apply {
                tvResultAmount.text = String.format(TEXT_VALUE_FORMAT, amount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.exchange_rates_item, parent, false),
            onItemClick
        )

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads[0] == true) {
                holder.bindAmount(getItem(position).amount)
            }
        }
    }

    companion object {
        private const val TEXT_VALUE_FORMAT = "%.2f"
    }
}