package com.jaaliska.exchangerates.presentation.ui.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.presentation.ui.rates.BaseRatesViewModel.Item
import com.jaaliska.exchangerates.databinding.ExchangeRatesItemBinding

class MainAdapter(
    private val onItemClick: (item: Item) -> Unit
) : ListAdapter<Item, MainAdapter.DataViewHolder>(Item.diffCallback) {

    inner class DataViewHolder(
        private val binding: ExchangeRatesItemBinding,
        private val onItemClick: (item: Item) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Item
        ) {
            itemView.apply {
                binding.title.text = item.title
                binding.subtitle.text = item.subtitle
                binding.tvResultAmount.text = String.format(TEXT_VALUE_FORMAT, item.amount)
                this.setOnClickListener { onItemClick(item) }
            }
        }

        fun bindAmount(amount: Double) {
            itemView.apply {
                binding.tvResultAmount.text = String.format(TEXT_VALUE_FORMAT, amount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ExchangeRatesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(
            binding,
            onItemClick
        )
    }

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