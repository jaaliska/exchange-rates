package com.jaaliska.exchangerates.presentation.ui.historical.year_choosing

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.jaaliska.exchangerates.R
import kotlinx.android.synthetic.main.year_picker_dialog.view.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class YearPickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    lateinit var selectableYear: MutableStateFlow<Int?>

    @RequiresApi(Build.VERSION_CODES.N)
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
                this@YearPickerFragment.dialog!!.cancel()
            }
        return builder.create()
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        lifecycleScope.launch {
            selectableYear.emit(year)
        }
    }

    companion object {
        private const val MIN_YEAR = 1990
        @RequiresApi(Build.VERSION_CODES.N)
        private val MAX_YEAR = getCurrentYear()

        fun newInstance(year: MutableStateFlow<Int?>): YearPickerFragment {
            return YearPickerFragment().apply {
                selectableYear = year
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        private fun getCurrentYear(): Int {
            val c = Calendar.getInstance()
            return c.get(Calendar.YEAR)
        }
    }
}
