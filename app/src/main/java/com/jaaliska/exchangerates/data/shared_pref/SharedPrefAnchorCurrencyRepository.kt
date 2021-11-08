package com.jaaliska.exchangerates.data.shared_pref

import android.content.SharedPreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.jaaliska.exchangerates.domain.repository.AnchorCurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SharedPrefAnchorCurrencyRepository(sharedPref: SharedPreferences) : AnchorCurrencyRepository {

    private val flowPref = FlowSharedPreferences(sharedPref)
    private val anchorCurrencyPref = flowPref.getString(key = "base_currency", defaultValue = "")

    override fun getAnchorCurrencyCode() = anchorCurrencyPref.asFlow()
    override fun setAnchorCurrencyCode(code: String) = run { anchorCurrencyPref.set(code) }
}