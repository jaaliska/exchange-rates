package com.jaaliska.exchangerates.data.repository

import android.content.SharedPreferences
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository

class SharedPreferencesRepository(private val sharedPreferences: SharedPreferences):
    PreferencesRepository
{
    override fun getBaseCurrencyCode(): String {
        return sharedPreferences.getString(BASE_CURRENCY_CODE_KEY, DEFAULT_BASE_CURRENCY_CODE)!!
    }

    override fun setBaseCurrencyCode(value: String) {
        sharedPreferences
            .edit()
            .putString(BASE_CURRENCY_CODE_KEY, value)
            .apply()
    }

    companion object {
        const val BASE_CURRENCY_CODE_KEY = "base_currency"
        const val DEFAULT_BASE_CURRENCY_CODE = ""
    }
}