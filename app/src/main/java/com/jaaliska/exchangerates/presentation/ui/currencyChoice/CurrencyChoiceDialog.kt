package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.utils.observe
import kotlinx.android.synthetic.main.dialog_currency_choice.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyChoiceDialog : DialogFragment() {

    private val viewModel by viewModel<CurrencyChoiceDialogViewModel>()


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
        currencyContainer.layoutManager = LinearLayoutManager(context)
        viewModel.currencies.observe(viewLifecycleOwner) { supportedCurrencies ->
            currencyContainer.adapter =
                CurrencyChoiceAdapter(supportedCurrencies) { code, isChecked ->
                    viewModel.onItemClick(code, isChecked)
                }
        }
        buttonOk.setOnClickListener {
            viewModel.onOkClick()
            dismiss()
        }
        buttonCancel.setOnClickListener {
            dismiss()
        }
    }
}