package com.jaaliska.exchangerates.presentation.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
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

    private val viewModel by viewModel<BaseHomeViewModel>()
    private val mainAdapter by lazy {
        MainAdapter(
            onItemClick = viewModel::onItemSelection
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        viewModel.updateDate.observe(viewLifecycleOwner) {
            if (it != null) {
                updateData.text = getString(
                    R.string.the_last_update_was_at,
                    SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(it)
                )
            }
        }
        setupAnchor()
        setupRecyclerView()

        swipeRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefresh()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            swipeRefresh.isRefreshing = it
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, requireContext().getString(it), Toast.LENGTH_LONG).show()
        }
        viewModel.currencyChoiceDialog.observe(viewLifecycleOwner) {
            it.show(parentFragmentManager, CURRENCY_CHOICE_DIALOG)
        }
    }

    private fun setupAnchor() {
        val currentDoubleValue = currencyAmount.text.toString().toDoubleOrNull()
        viewModel.anchor.observe(viewLifecycleOwner) {
            if (it != null && it.amount != currentDoubleValue) {
                title.text = it.title
                subtitle.text = it.subtitle
                val nf: NumberFormat = NumberFormat.getInstance()
                nf.maximumFractionDigits = 2
                nf.isGroupingUsed = false
                currencyAmount.setText(nf.format(it.amount))
            }
        }

        currencyAmount.filters = arrayOf(MoneyValueFilter())
        currencyAmount.doAfterTextChanged {
            val newAmount = it.toString().toDoubleOrNull()
            newAmount?.let { viewModel.onAmountChanged(newAmount) }
        }
    }

    private fun setupRecyclerView() {
        ratesContainer.adapter = mainAdapter
        ratesContainer.layoutManager = LinearLayoutManager(context)
        viewModel.items.observe(viewLifecycleOwner) {
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