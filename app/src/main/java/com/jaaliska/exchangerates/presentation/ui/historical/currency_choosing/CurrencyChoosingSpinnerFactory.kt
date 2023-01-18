package com.jaaliska.exchangerates.presentation.ui.historical.currency_choosing

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.utils.setBlinkingAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CurrencyChoosingSpinnerFactory(
    context: Context,
    spinner: Spinner,
    data: List<String>,
    returnedData: MutableSharedFlow<String?>,
    tittleText: String
) {
    private val items =
        listOf(CurrencyChoosingSpinnerAdapter.NO_SELECTED_ITEM, *data.toTypedArray())

    init {
        var check = 0
        spinner.adapter = CurrencyChoosingSpinnerAdapter(
            context,
            tittleText,
            items
        )

        val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (++check > 1) {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (position == 0) {
                            returnedData.emit(null)
                        } else {
                            returnedData.emit(items[position - 1])
                        }
                    }
                } else {
                    view?.findViewById<TextView>(R.id.subtitleTxt)?.let { setBlinkingAnimation(it) }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        spinner.onItemSelectedListener = listener
    }
}
