package com.jaaliska.exchangerates.presentation.ui.screens.currency_choice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.common.list.checkable_item.CheckableItemAdapter
import com.jaaliska.exchangerates.presentation.utils.observe
import kotlinx.android.synthetic.main.dialog_currency_choice.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyChoiceDialog : DialogFragment() {

    private val viewModel by viewModel<BaseCurrencyChoiceViewModel>()
    private val adapter by lazy {
        CheckableItemAdapter { code, isChecked ->
            viewModel.onItemClick(code, isChecked)
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
            progressBar.visibility = if (it) ProgressBar.VISIBLE else ProgressBar.INVISIBLE
        }
        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(context, requireContext().getString(it), Toast.LENGTH_LONG).show()
            }
        }
        buttonOk.setOnClickListener { dismiss() }
    }
}