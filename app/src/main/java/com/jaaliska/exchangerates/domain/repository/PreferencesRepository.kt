package com.jaaliska.exchangerates.domain.repository

interface PreferencesRepository {
    fun getBaseCurrencyCode(): String
    fun setBaseCurrencyCode(value: String)
}