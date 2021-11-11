package com.jaaliska.exchangerates.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.RatesSnapshot
import com.jaaliska.exchangerates.presentation.utils.RatesDiffUtilCallback
import kotlinx.android.synthetic.main.exchange_rates_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainAdapter(
    private val baseCurrencyAmount: StateFlow<Double>,
    private val coroutineScope: CoroutineScope,
    private val onItemClick: (currencyCode: Currency) -> Unit
) : ListAdapter<RatesSnapshot.Rate, MainAdapter.DataViewHolder>(RatesDiffUtilCallback()) {

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var job: Job? = null
        private var rate: RatesSnapshot.Rate? = null
        private var baseCurrencyAmount: StateFlow<Double>? = null
        private var resultAmount: Double? = null

        fun bind(
            rate: RatesSnapshot.Rate,
            baseCurrencyAmount: StateFlow<Double>,
        ) {
            this.baseCurrencyAmount = baseCurrencyAmount
            this.rate = rate
            itemView.apply {
                title.text = rate.currency.code
                subtitle.text = rate.currency.name
                this.setOnClickListener {
                    onItemClick(rate.currency)
                }
            }
        }

        fun onViewAttachedToWindow() {
            itemView.apply {
                job = coroutineScope.launch {
                    baseCurrencyAmount?.collect {
                        resultAmount = rate?.rate?.times(it)
                        tvResultAmount.text = String.format("%.2f", resultAmount)
                    }
                }
            }
        }

        fun onViewDetachedFromWindow() {
            job?.cancel()
        }
    }

    override fun onViewAttachedToWindow(holder: DataViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: DataViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.exchange_rates_item, parent, false)
        )

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(currentList[position], baseCurrencyAmount)
    }

}