package com.jaaliska.exchangerates.domain.usecase

import com.jaaliska.exchangerates.domain.model.Currency

interface SetAnchorCurrencyUseCase {
    suspend operator fun invoke(currency: Currency)
}