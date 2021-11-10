package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.anchor_currency.SharedPrefAnchorCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecase.SetAnchorCurrencyUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SetAnchorCurrencyUseCaseImpl(
    private val anchorCurrencyRepository: SharedPrefAnchorCurrencyRepository
) :
    SetAnchorCurrencyUseCase {

    override suspend fun invoke(currency: Currency) {
        anchorCurrencyRepository.update(code = currency.code)
    }
}