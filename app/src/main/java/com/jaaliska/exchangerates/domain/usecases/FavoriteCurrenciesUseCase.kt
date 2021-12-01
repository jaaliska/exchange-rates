package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.model.Currency
import kotlin.jvm.Throws


interface FavoriteCurrenciesUseCase {
    @Throws(IllegalFavoritesCountException::class)
    suspend fun set(codes: Set<String>)
    suspend fun get(): List<Currency>
}