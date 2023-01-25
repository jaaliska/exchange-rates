package com.jaaliska.exchangerates.presentation.ui.historical.year_choosing

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.jaaliska.exchangerates.R
import kotlinx.android.synthetic.main.year_picker_dialog.view.*
import java.util.*

class YearPickerDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {

    lateinit var setYear: (Int?) -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context, android.R.style.ThemeOverlay_Material_Dialog)
        val view = layoutInflater.inflate(R.layout.year_picker_dialog, null)
        view.pickerYear.apply {
            minValue = MIN_YEAR
            maxValue = MAX_YEAR
            value = MAX_YEAR
        }
        builder.setView(view)
            .setPositiveButton(R.string.ok) { dialog, id ->
                onDateSet(null, view.pickerYear.value, 1, 1)
            }.setNegativeButton(R.string.cancel) { dialog, id ->
                this@YearPickerDialog.dialog!!.cancel()
            }
        return builder.create()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        lifecycleScope.launchWhenStarted {
            setYear(year)
        }
    }

    companion object {
        private const val MIN_YEAR = 1990
        private val MAX_YEAR = getCurrentYear()

        fun newInstance(year: (Int?) -> Unit): YearPickerDialog {
            return YearPickerDialog().apply {
                setYear = year
            }
        }

        private fun getCurrentYear(): Int {
            val c = Calendar.getInstance()
            return c.get(Calendar.YEAR)
        }
    }
}
