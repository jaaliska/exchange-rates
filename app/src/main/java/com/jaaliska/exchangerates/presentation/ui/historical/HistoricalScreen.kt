package com.jaaliska.exchangerates.presentation.ui.historical

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.historical.currency_choosing.CurrencyChoosingSpinnerFactory
import com.jaaliska.exchangerates.presentation.ui.historical.year_choosing.YearPickerFragment
import com.jaaliska.exchangerates.presentation.utils.observe
import com.jaaliska.exchangerates.presentation.utils.setBlinkingAnimation
import kotlinx.android.synthetic.main.fragment_screen_historical.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoricalScreen : Fragment(R.layout.fragment_screen_historical) {

    private val viewModel by viewModel<BaseHistoricalViewModel>()
    private val currencies = listOf("USD", "EUR", "PLN", "BUN", "GRN")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        selectDateLayout.setOnClickListener {
            showDateDialog()
        }

        setupSpinners()

        viewModel.selectableYear.observe(viewLifecycleOwner) {
            if (it != null) {
                selectedYear.animation?.cancel()
                selectedYear.text = getString(R.string.yearFormat,it.toString())
                selectedYear.setTextColor(resources.getColor(R.color.primaryColor, requireContext().theme))
            } else {
                setBlinkingAnimation(selectedYear)
            }
        }
    }

    private fun setupSpinners() {
        CurrencyChoosingSpinnerFactory(
            requireContext(),
            spinnerFrom,
            currencies,
            viewModel.currencyFrom,
            getString(R.string.from)
        )
        CurrencyChoosingSpinnerFactory(
            requireContext(),
            spinnerTo,
            currencies,
            viewModel.currencyTo,
            getString(R.string.to)
        )
    }

    private fun showDateDialog() {
        YearPickerFragment.newInstance(viewModel.selectableYear)
            .show(parentFragmentManager, "YearPickerFragment")
    }
}