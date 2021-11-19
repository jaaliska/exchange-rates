package com.jaaliska.exchangerates.domain.usecase

import com.jaaliska.exchangerates.domain.model.Currency
import kotlin.jvm.Throws

interface UpdateCurrencyFavoriteStateUseCase {

    @Throws(Exception::class)
    suspend operator fun invoke(currency: Currency, isFavorite: Boolean)
}