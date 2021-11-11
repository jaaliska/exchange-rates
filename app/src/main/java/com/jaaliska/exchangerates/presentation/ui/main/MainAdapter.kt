package com.jaaliska.exchangerates.presentation.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.main.BaseHomeViewModel.Item
import kotlinx.android.synthetic.main.exchange_rates_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

typealias ItemClickCallback = (tittle: String, resultAmount: Double) -> Unit

class MainAdapter(
    private val baseAmount: Flow<Double>,
    private val coroutineScope: CoroutineScope,
    private val onItemClick: ItemClickCallback
) : ListAdapter<Item, MainAdapter.DataViewHolder>(Item.diffCallback) {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var job: Job? = null
        private var item: Item? = null
        private var baseAmount: Flow<Double>? = null
        private var coroutineScope: CoroutineScope? = null
        private var resultAmount: Double? = null

        fun bind(
            item: Item,
            baseCurrencyAmount: Flow<Double>,
            coroutineScope: CoroutineScope,
            onItemClick: ItemClickCallback
        ) {
            this.coroutineScope = coroutineScope
            this.baseAmount = baseCurrencyAmount
            this.item = item
            itemView.apply {
                title.text = item.title
                subtitle.text = item.subtitle
                this.setOnClickListener {
                    val resAmount = resultAmount
                    resAmount?.let { onItemClick(item.title, resAmount) }
                }
            }
        }

        fun onViewAttachedToWindow() {
            itemView.apply {
                job = coroutineScope?.launch {
                    baseAmount?.collect {
                        resultAmount = item?.amount?.times(it)
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
        holder.bind(currentList[position], baseAmount, coroutineScope, onItemClick)
    }

}