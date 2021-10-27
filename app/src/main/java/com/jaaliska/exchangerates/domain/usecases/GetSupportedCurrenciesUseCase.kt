package com.jaaliska.exchangerates.domain.usecases

import com.jaaliska.exchangerates.domain.model.Currency

interface GetSupportedCurrenciesUseCase {

    suspend operator fun invoke(forceUpdate: Boolean): List<Currency>
}