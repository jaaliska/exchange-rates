package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.domain.model.CurrencyDetails
import kotlinx.coroutines.flow.MutableStateFlow

class CurrencyChoiceDialog(

) : DialogFragment() {

    var supportedCurrencies: MutableStateFlow<CurrencyDetails>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_currency_choice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {

    }

    companion object {
        fun newInstance(date: MutableStateFlow<CurrencyDetails>): CurrencyChoiceDialog {
            return CurrencyChoiceDialog()
                .apply {
                    supportedCurrencies = date
                }
        }
    }
}