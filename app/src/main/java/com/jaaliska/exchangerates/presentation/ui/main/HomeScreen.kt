package com.jaaliska.exchangerates.presentation.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.utils.MoneyValueFilter
import kotlinx.android.synthetic.main.fragment_screen_home.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat

class HomeScreen : Fragment(R.layout.fragment_screen_home) {

    private val viewModel by viewModel<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        lifecycleScope.launch {
            viewModel.baseCurrencyDetails.collect {
                if (it != null) {
                    currencyCode.text = it.currencyCode
                    currencyName.text = it.currencyName
                }
            }
        }
        lifecycleScope.launch {
            viewModel.baseCurrencyAmount.collect {
                val currentDoubleValue = currencyAmount.text.toString().toDoubleOrNull()
                if (currentDoubleValue != it) {
                    val nf: NumberFormat = NumberFormat.getInstance()
                    nf.maximumFractionDigits = 2
                    nf.isGroupingUsed = false
                    currencyAmount.setText(nf.format(it))
                }
            }
        }

        currencyAmount.filters = arrayOf(MoneyValueFilter())
        currencyAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val doubleValue = s.toString().toDoubleOrNull()
                if (doubleValue != null && viewModel.baseCurrencyAmount.value != doubleValue) {
                    lifecycleScope.launch {
                        viewModel.baseCurrencyAmount.emit(doubleValue)
                    }
                }
            }
        })

        //RecyclerView
        ratesContainer.layoutManager = LinearLayoutManager(context)
        lifecycleScope.launch {
            viewModel.exchangeRates.collect {
                ratesContainer.adapter = MainAdapter(
                    rates = it,
                    baseCurrencyAmount = viewModel.baseCurrencyAmount,
                    coroutineScope = lifecycleScope,
                    onItemClick = viewModel::onCurrencySelection
                )
            }
        }

        ratesContainer.addItemDecoration(
            DividerItemDecoration(
                ratesContainer.context,
                (ratesContainer.layoutManager as LinearLayoutManager).orientation
            )
        )
    }
}