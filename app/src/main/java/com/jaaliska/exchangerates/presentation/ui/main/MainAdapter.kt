package com.jaaliska.exchangerates.presentation.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.main.BaseHomeViewModel.NamedRate
import kotlinx.android.synthetic.main.exchange_rates_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

typealias ItemClickCallback = (currencyCode: String, resultAmount: Double) -> Unit

class MainAdapter(
    private val baseCurrencyAmount: Flow<Double>,
    private val coroutineScope: CoroutineScope,
    private val onItemClick: ItemClickCallback
) : ListAdapter<NamedRate, MainAdapter.DataViewHolder>(NamedRate.diffCallback) {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var job: Job? = null
        private var rate: NamedRate? = null
        private var baseCurrencyAmount: Flow<Double>? = null
        private var coroutineScope: CoroutineScope? = null
        private var resultAmount: Double? = null

        fun bind(
            rate: NamedRate,
            baseCurrencyAmount: Flow<Double>,
            coroutineScope: CoroutineScope,
            onItemClick: ItemClickCallback
        ) {
            this.coroutineScope = coroutineScope
            this.baseCurrencyAmount = baseCurrencyAmount
            this.rate = rate
            itemView.apply {
                title.text = rate.currencyCode
                subtitle.text = rate.currencyName
                this.setOnClickListener {
                    val resAmount = resultAmount
                    resAmount?.let { onItemClick(rate.currencyCode, resAmount) }
                }
            }
        }

        fun onViewAttachedToWindow() {
            itemView.apply {
                job = coroutineScope?.launch {
                    baseCurrencyAmount?.collect {
                        resultAmount = rate?.exchangeRate?.times(it)
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
        holder.bind(currentList[position], baseCurrencyAmount, coroutineScope, onItemClick)
    }

}