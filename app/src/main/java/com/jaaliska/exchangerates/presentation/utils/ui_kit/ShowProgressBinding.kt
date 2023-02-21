package com.jaaliska.exchangerates.presentation.utils.ui_kit

import android.content.Context
import androidx.lifecycle.*
import com.jaaliska.exchangerates.presentation.utils.observe
import kotlinx.coroutines.flow.Flow

class ShowProgressBinding(
    isLoading: Flow<Boolean>,
    lifecycleOwner: LifecycleOwner,
    private val context: Context
) {

    init {
        isLoading.observe(lifecycleOwner) {
            if (it) ProgressDialog.showProgress(context)
            else ProgressDialog.hideProgress()
        }
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    ProgressDialog.hideProgress()
                }
            }
        })
    }
}