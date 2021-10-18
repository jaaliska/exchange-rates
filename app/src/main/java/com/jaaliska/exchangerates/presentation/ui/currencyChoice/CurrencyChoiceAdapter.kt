package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.model.CurrencyDetails

class CurrencyChoiceAdapter(
    private val supportedCurrencies: List<CurrencyDetails>,
    private val onItemClick: (currencyCode: String) -> Unit
) : RecyclerView.Adapter<CurrencyChoiceAdapter.CheckableViewHolder>() {

    class CheckableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            supportedCurrencies: CurrencyDetails,
            onItemClick: (currencyCode: String) -> Unit
        ) {
            itemView.apply {

                this.setOnClickListener {

                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableViewHolder =
        CheckableViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.exchange_rates_item, parent, false)
        )

    override fun getItemCount(): Int {
        return supportedCurrencies.size
    }

    override fun onBindViewHolder(holder: CheckableViewHolder, position: Int) {
        holder.bind(supportedCurrencies[position], onItemClick)
    }

}