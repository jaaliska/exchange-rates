package com.jaaliska.exchangerates.presentation.ui.rates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog
import com.jaaliska.exchangerates.presentation.utils.MoneyValueFilter
import com.jaaliska.exchangerates.presentation.utils.observe
import com.jaaliska.exchangerates.databinding.FragmentScreenRatesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class RatesScreen : Fragment() {

    private val viewModel by viewModel<BaseRatesViewModel>()
    private val mainAdapter by lazy {
        MainAdapter(
            onItemClick = viewModel::onItemSelection
        )
    }
    private lateinit var binding: FragmentScreenRatesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreenRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupVisibility()
        setupUI()
    }

    private fun setupVisibility() {
        viewModel.isHasFavorites.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    binding.rootExchangeRates.isVisible = true
                    binding.rootHaveNoFavorites.isVisible = false
                } else {
                    binding.rootHaveNoFavorites.isVisible = true
                    binding.rootExchangeRates.isVisible = false
                    binding.butAddFavorites.setOnClickListener {
                        CurrencyChoiceDialog().show(parentFragmentManager, CURRENCY_CHOICE_DIALOG)
                    }
                }
            }
        }
    }

    private fun setupUI() {
        viewModel.updateDate.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.updateData.text = getString(
                    R.string.the_last_update_was_at,
                    SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(it)
                )
            }
        }
        setupAnchor()
        setupRecyclerView()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.onSwipeToRefresh()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, requireContext().getString(it), Toast.LENGTH_LONG).show()
        }
    }

    private fun setupAnchor() {
        val currentDoubleValue = binding.currencyAmount.text.toString().toDoubleOrNull()
        viewModel.anchor.observe(viewLifecycleOwner) {
            if (it != null && it.amount != currentDoubleValue) {
                binding.title.text = it.title
                binding.subtitle.text = it.subtitle
                val nf: NumberFormat = NumberFormat.getInstance()
                nf.maximumFractionDigits = 2
                nf.isGroupingUsed = false
                binding.currencyAmount.setText(nf.format(it.amount))
            }
        }

        binding.currencyAmount.filters = arrayOf(MoneyValueFilter())
        binding.currencyAmount.doAfterTextChanged {
            val newAmount = it.toString().toDoubleOrNull()
            newAmount?.let { viewModel.onAmountChanged(newAmount) }
        }
    }

    private fun setupRecyclerView() {
        binding.ratesContainer.adapter = mainAdapter
        binding.ratesContainer.layoutManager = LinearLayoutManager(context)
        viewModel.items.observe(viewLifecycleOwner) {
            mainAdapter.submitList(it)
        }
    }

    companion object {
        const val DATE_FORMAT = "dd-MM-yyyy HH:mm:ss"
        const val CURRENCY_CHOICE_DIALOG = "CurrencyChoiceDialog"
    }
}