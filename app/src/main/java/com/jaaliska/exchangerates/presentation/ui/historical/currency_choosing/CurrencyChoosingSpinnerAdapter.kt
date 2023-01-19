package com.jaaliska.exchangerates.presentation.ui.historical.currency_choosing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.utils.setBlinkingAnimation

class CurrencyChoosingSpinnerAdapter(
    context: Context,
    private var tittleText: String,
    items: List<String>
) :
    ArrayAdapter<String>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: LayoutInflater.from(parent.context)
                .inflate(R.layout.header_currency_choosing, parent, false)
        getItem(position)?.let { value ->
            setupItem(view, value)
        }
        return view
    }

    private fun setupItem(view: View, value: String) {
        view.findViewById<TextView>(R.id.titleTxt).text = tittleText
        if (value == NO_SELECTED_ITEM) {
            view.isActivated = false
            setBlinkingAnimation(view.findViewById<TextView>(R.id.subtitleTxt))
        } else {
            view.findViewById<TextView>(R.id.currencyTxt).text = value
            view.isActivated = true
        }
    }

    companion object {
        const val NO_SELECTED_ITEM = "-"
    }
}
