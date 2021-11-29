package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import app.cash.turbine.test
import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.const.TestData.listCurrencies
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CurrencyChoiceDialogViewModelTest : BaseTestCase() {

    private val getSupportedCurrenciesUseCase = mock(GetSupportedCurrenciesUseCase::class.java)
//    private val favoriteCurrenciesUseCase =

    @Test
    fun testAddItems(): Unit = runBlocking {
//        `when`(getSupportedCurrenciesUseCase(true)).thenReturn(listCurrencies)
        val useCase = mock<FavoriteCurrenciesUseCase> {
            onBlocking { get() }.thenReturn(listOf())
        }
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase, useCase
        )
        verify(useCase).get()
        currencyChoiceDialogViewModel.onItemClick("AMD", true)
        currencyChoiceDialogViewModel.onItemClick("ALL", true)
        currencyChoiceDialogViewModel.onItemClick("AED", true)
        currencyChoiceDialogViewModel.onItemClick("AFN", true)

        currencyChoiceDialogViewModel.isLoading.test {
            assertEquals(false, awaitItem())
            currencyChoiceDialogViewModel.onOkClick {}
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
            expectNoEvents()
        }

        val expectedList = setOf("AMD", "ALL", "AED", "AFN")
        verify(useCase).set(expectedList)
    }

    @Test
    fun testRemoveItems(): Unit = runBlocking {
        `when`(getSupportedCurrenciesUseCase(true)).thenReturn(listCurrencies)
        `when`(favoriteCurrenciesUseCase.get()).thenReturn(listCurrencies)
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase, favoriteCurrenciesUseCase
        )
        verify(favoriteCurrenciesUseCase).get()
        currencyChoiceDialogViewModel.onItemClick("AFN", false)
        currencyChoiceDialogViewModel.onItemClick("ALL", false)

        currencyChoiceDialogViewModel.isLoading.test {
            assertEquals(false, awaitItem())
            currencyChoiceDialogViewModel.onOkClick {}
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
        }

        val expectedList = setOf("AMD", "AED")
        verify(favoriteCurrenciesUseCase).set(expectedList)
    }


    @Test
    fun testLessThenTwoItemsAreSelected(): Unit = runBlocking {
        `when`(getSupportedCurrenciesUseCase(true)).thenReturn(listCurrencies)
        `when`(favoriteCurrenciesUseCase.get()).thenReturn(listOf())
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase, favoriteCurrenciesUseCase
        )
        verify(favoriteCurrenciesUseCase).get()
        currencyChoiceDialogViewModel.onItemClick("AMD", true)
        currencyChoiceDialogViewModel.errors.test {
            currencyChoiceDialogViewModel.onOkClick {}
            assertEquals(R.string.not_enough_changed_currency, awaitItem())
            expectNoEvents()
        }
    }


    private suspend fun waitForFalse(flow: StateFlow<Boolean>) {
        if (!flow.value) return
        flow.filter { !it }.take(1).collect()
    }
}