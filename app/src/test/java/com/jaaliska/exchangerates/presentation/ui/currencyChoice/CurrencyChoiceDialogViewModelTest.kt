package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import app.cash.turbine.test
import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.const.TestData
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import org.junit.Test
import org.mockito.Mockito

class CurrencyChoiceDialogViewModelTest : BaseTestCase() {

    private val getSupportedCurrenciesUseCase = Mockito.mock(GetSupportedCurrenciesUseCase::class.java)
    private val favoriteCurrenciesUseCase = Mockito.mock(FavoriteCurrenciesUseCase::class.java)

    @Test
    fun testSelectItems(): Unit = runBlocking {
        Mockito.`when`(favoriteCurrenciesUseCase.get()).thenReturn(TestData.listCurrencies)
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase, favoriteCurrenciesUseCase
        )
        waitForFalse(currencyChoiceDialogViewModel.isLoading)
        currencyChoiceDialogViewModel.isLoading.test{

        }
        currencyChoiceDialogViewModel.isLoading
        currencyChoiceDialogViewModel.onItemClick("AMD", true)
        currencyChoiceDialogViewModel.onItemClick("AED", true)
        currencyChoiceDialogViewModel.onItemClick("AFN", true)
        currencyChoiceDialogViewModel.onItemClick("ALL", true)
        currencyChoiceDialogViewModel.onOkClick()

       // Mockito.verify(favoriteCurrenciesUseCase).set(TestData.listCurrencies.map { it.code })

    }

    private suspend fun waitForFalse(flow: StateFlow<Boolean>) {
        if (!flow.value) return
        flow.filter { !it }.take(1).collect()
    }
}