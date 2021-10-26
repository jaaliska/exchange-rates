package com.jaaliska.exchangerates.domain.repository

interface FavoriteCurrenciesRepository {
    suspend fun readFavoriteCurrencies(): List<String> // list of currency codes
    suspend fun saveFavoriteCurrencies(currencyCodes: List<String>)
}