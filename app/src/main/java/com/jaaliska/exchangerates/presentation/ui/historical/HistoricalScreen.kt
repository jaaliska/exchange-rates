package com.jaaliska.exchangerates.presentation.ui.historical

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.historical.currency_choosing.CurrencyChoosingSpinnerFactory
import com.jaaliska.exchangerates.presentation.ui.historical.year_choosing.YearPickerDialog
import com.jaaliska.exchangerates.presentation.utils.observe
import com.jaaliska.exchangerates.presentation.utils.setBlinkingAnimation
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