package com.jaaliska.exchangerates.data.anchor_currency

import android.content.SharedPreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.jaaliska.exchangerates.domain.datasource.AnchorCurrencyDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SharedPrefAnchorCurrencyRepository(sharedPref: SharedPreferences) : AnchorCurrencyDataSource {

    private val flowPref = FlowSharedPreferences(sharedPref)
    private val anchorCurrencyPref = flowPref.getString(key = "base_currency", defaultValue = "")


    override fun observe() = anchorCurrencyPref.asFlow()
    fun update(code: String) = run { anchorCurrencyPref.set(code) }
}