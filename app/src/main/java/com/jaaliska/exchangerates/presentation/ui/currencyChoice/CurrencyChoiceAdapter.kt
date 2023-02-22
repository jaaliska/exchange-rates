package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.databinding.CurrencyChoiceItemBinding
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.BaseCurrencyChoiceDialogViewModel.SelectableItem

class CurrencyChoiceAdapter(
    private val onItemClick: (item: SelectableItem, isCheck: Boolean) -> Unit
) : ListAdapter<SelectableItem, CurrencyChoiceAdapter.CheckableViewHolder>(
    SelectableItem.diffCallback
) {

    class CheckableViewHolder(private val binding: CurrencyChoiceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: SelectableItem,
            onItemClick: (item: SelectableItem, isCheck: Boolean) -> Unit
        ) {
            itemView.apply {
                binding.title.text = item.title
                binding.subtitle.text = item.subtitle
                binding.checkbox.isChecked = item.isSelected

                this.setOnClickListener {
                    onItemClick(item, !item.isSelected)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableViewHolder {
        val binding =
            CurrencyChoiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckableViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: CheckableViewHolder, position: Int) {
        holder.bind(currentList[position], onItemClick)
    }

}