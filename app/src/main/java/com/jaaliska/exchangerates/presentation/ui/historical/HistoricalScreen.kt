package com.jaaliska.exchangerates.presentation.ui.historical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.databinding.FragmentScreenHistoricalBinding
import com.jaaliska.exchangerates.presentation.ui.historical.currency_choosing.CurrencyChoosingSpinnerFactory
import com.jaaliska.exchangerates.presentation.ui.historical.year_choosing.YearPickerDialog
import com.jaaliska.exchangerates.presentation.utils.observe
import com.jaaliska.exchangerates.presentation.utils.ui_kit.setBlinkingAnimation
import com.jaaliska.exchangerates.presentation.utils.ui_kit.showProgress
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoricalScreen : Fragment() {

    private val viewModel by viewModel<BaseHistoricalViewModel>()
    private lateinit var binding: FragmentScreenHistoricalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreenHistoricalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        viewModel.favoriteCurrencyCodes.observe(viewLifecycleOwner) {
            setupSpinners(it)
        }
        binding.selectDateLayout.setOnClickListener {
            showDateDialog()
        }

        viewModel.selectedYear.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.selectedYear.animation?.cancel()
                binding.selectedYear.text = getString(R.string.yearFormat, it.toString())
                binding.selectedYear.setTextColor(
                    resources.getColor(
                        R.color.primaryColor,
                        requireContext().theme
                    )
                )
            } else {
                setBlinkingAnimation(binding.selectedYear)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(
                context,
                requireContext().getString(it ?: R.string.something_went_wrong),
                Toast.LENGTH_LONG
            ).show()
        }

        requireContext().showProgress(viewModel.isLoading, this)

        viewModel.currenciesForChart.observe(viewLifecycleOwner) { currencies ->
            if (currencies != null) {
                val entries = mutableListOf<Entry>()
                for (data in currencies) {
                    entries.add(
                        Entry(
                            data.date.month.toFloat() + 1,
                            data.rates.first().exchangeRate.toFloat()
                        )
                    )
                }

                val dataSet = LineDataSet(entries, getString(R.string.chart_label))
                binding.chart.data = LineData(dataSet)
                binding.chart.invalidate()
            }
        }
    }

    private fun setupSpinners(items: List<String>) {
        CurrencyChoosingSpinnerFactory(
            requireContext(),
            binding.spinnerFrom,
            items,
            viewModel::onCurrencyFromSelected,
            getString(R.string.from)
        )
        CurrencyChoosingSpinnerFactory(
            requireContext(),
            binding.spinnerTo,
            items,
            viewModel::onCurrencyToSelected,
            getString(R.string.to)
        )

    }

    private fun showDateDialog() {
        YearPickerDialog.newInstance(viewModel::onYearSelected)
            .show(parentFragmentManager, "YearPickerFragment")
    }
}