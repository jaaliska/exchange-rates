package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.model.SelectedCurrency
import com.jaaliska.exchangerates.presentation.utils.SelectedCurrencyDiffUtilCallback
import kotlinx.android.synthetic.main.currency_choice_item.view.*

class CurrencyChoiceAdapter(
    private val onItemClick: (currencyCode: String, isCheck: Boolean) -> Unit
) : ListAdapter<SelectedCurrency, CurrencyChoiceAdapter.CheckableViewHolder>(
    SelectedCurrencyDiffUtilCallback()
) {

    class CheckableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(
            supportedCurrencies: SelectedCurrency,
            onItemClick: (currencyCode: String, isCheck: Boolean) -> Unit
        ) {
            itemView.apply {
                currencyCode.text = supportedCurrencies.code
                currencyName.text = supportedCurrencies.name
                checkboxCurrency.isChecked = supportedCurrencies.isSelected
                this.setOnClickListener {
                    val changedValue = !checkboxCurrency.isChecked
                    onItemClick(supportedCurrencies.code, changedValue)
                    supportedCurrencies.isSelected = changedValue
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