package com.jaaliska.exchangerates.presentation.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.utils.MoneyValueFilter
import com.jaaliska.exchangerates.presentation.utils.observe
import kotlinx.android.synthetic.main.fragment_screen_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class HomeScreen : Fragment(R.layout.fragment_screen_home) {

    private val viewModel by viewModel<HomeViewModel>()
    private val mainAdapter by lazy {
        MainAdapter(
            baseCurrencyAmount = viewModel.baseCurrencyAmount,
            coroutineScope = lifecycleScope,
            onItemClick = viewModel::onCurrencySelection
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        viewModel.baseCurrencyDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                title.text = it.code
                subtitle.text = it.name
            }
        }

        ratesContainer.adapter = mainAdapter

        viewModel.updateDate.observe(viewLifecycleOwner) {
            if (it != null) {
                updateData.text = getString(
                    R.string.the_last_update_was_at,
                    SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(it)
                )
            }
        }
        setupEditTextProcessing()
        setupRecyclerView()

        swipeRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefresh()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            swipeRefresh.isRefreshing = it
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, requireContext().getString(it), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupEditTextProcessing() {
        viewModel.baseCurrencyAmount.observe(viewLifecycleOwner) {
            val currentDoubleValue = currencyAmount.text.toString().toDoubleOrNull()
            if (currentDoubleValue != it) {
                val nf: NumberFormat = NumberFormat.getInstance()
                nf.maximumFractionDigits = 2
                nf.isGroupingUsed = false
                currencyAmount.setText(nf.format(it))
            }
        }

        currencyAmount.filters = arrayOf(MoneyValueFilter())
        currencyAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val doubleValue = s.toString().toDoubleOrNull()
                if (doubleValue != null && viewModel.baseCurrencyAmount.value != doubleValue) {
                    viewModel.baseCurrencyAmount.value = doubleValue
                }
            }
        })
    }

    private fun setupRecyclerView() {
        ratesContainer.layoutManager = LinearLayoutManager(context)

        viewModel.exchangeRates.observe(viewLifecycleOwner) {
            mainAdapter.submitList(it)
        }

        ratesContainer.addItemDecoration(
            DividerItemDecoration(
                ratesContainer.context,
                (ratesContainer.layoutManager as LinearLayoutManager).orientation
            )
        )
    }

    companion object {
        const val DATE_FORMAT = "dd-MM-yyyy HH:mm:ss"
        const val CURRENCY_CHOICE_DIALOG = "CurrencyChoiceDialog"
    }
}