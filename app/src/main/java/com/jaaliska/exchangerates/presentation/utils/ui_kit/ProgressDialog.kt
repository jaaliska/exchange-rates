package com.jaaliska.exchangerates.presentation.utils.ui_kit

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.jaaliska.exchangerates.R

object ProgressDialog {
    private var dialog: Dialog? = null

    fun showProgress(context: Context, cancelable: Boolean = false) {
        if (dialog != null) return
        dialog = createDialog(context, cancelable)
    }

    private fun createDialog(context: Context, cancelable: Boolean): Dialog {
        return Dialog(context).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.layout_progress_dialog)
            setCancelable(cancelable)
            setCanceledOnTouchOutside(cancelable)
            show()
        }
    }

    fun hideProgress() {
        dialog?.run {
            dialog = null
            dismiss()
        }
    }
}