package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.utils.observe
import kotlinx.android.synthetic.main.dialog_currency_choice.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyChoiceDialog : DialogFragment() {

    private val viewModel by viewModel<BaseCurrencyChoiceDialogViewModel>()
    private val adapter by lazy {
        CurrencyChoiceAdapter { item, isChecked ->
            viewModel.onItemSelected(item, isChecked)
        }
    }

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

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    private fun setupView() {
        currencyContainer.layoutManager = LinearLayoutManager(context)
        currencyContainer.adapter = adapter
        viewModel.items.observe(viewLifecycleOwner) { supportedCurrencies ->
            adapter.submitList(supportedCurrencies)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            progressBar.isVisible = it
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, requireContext().getString(it), Toast.LENGTH_LONG).show()
        }
        buttonOk.setOnClickListener {
            viewModel.submitItems { dismiss() }
        }
        buttonCancel.setOnClickListener {
            dismiss()
        }
    }
}