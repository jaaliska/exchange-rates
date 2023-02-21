package com.jaaliska.exchangerates.presentation.ui.historical

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.historical.currency_choosing.CurrencyChoosingSpinnerFactory
import com.jaaliska.exchangerates.presentation.ui.historical.year_choosing.YearPickerDialog
import com.jaaliska.exchangerates.presentation.utils.observe
import com.jaaliska.exchangerates.presentation.utils.ui_kit.setBlinkingAnimation
import com.jaaliska.exchangerates.presentation.utils.ui_kit.showProgress
import kotlinx.android.synthetic.main.fragment_screen_historical.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoricalScreen : Fragment(R.layout.fragment_screen_historical) {

    private val viewModel by viewModel<BaseHistoricalViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        viewModel.favoriteCurrencyCodes.observe(viewLifecycleOwner) {
            setupSpinners(it)
        }
        selectDateLayout.setOnClickListener {
            showDateDialog()
        }

        viewModel.selectedYear.observe(viewLifecycleOwner) {
            if (it != null) {
                selectedYear.animation?.cancel()
                selectedYear.text = getString(R.string.yearFormat, it.toString())
                selectedYear.setTextColor(
                    resources.getColor(
                        R.color.primaryColor,
                        requireContext().theme
                    )
                )
            } else {
                setBlinkingAnimation(selectedYear)
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
                chart.data = LineData(dataSet)
                chart.invalidate()
            }
        }
    }

    private fun setupSpinners(items: List<String>) {
        CurrencyChoosingSpinnerFactory(
            requireContext(),
            spinnerFrom,
            items,
            viewModel::onCurrencyFromSelected,
            getString(R.string.from)
        )
        CurrencyChoosingSpinnerFactory(
            requireContext(),
            spinnerTo,
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