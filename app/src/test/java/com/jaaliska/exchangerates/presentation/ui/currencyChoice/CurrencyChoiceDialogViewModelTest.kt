package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import app.cash.turbine.test
import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.const.TestData.listCurrencies
import com.jaaliska.exchangerates.const.TestData.listCurrencyItemsAllFalse
import com.jaaliska.exchangerates.const.TestData.listCurrencyItemsAllTrue
import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.usecases.FavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetSupportedCurrenciesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CurrencyChoiceDialogViewModelTest : BaseTestCase() {

    @Test
    fun testAddAndSaveItems(): Unit = runBlocking {
        val getSupportedCurrenciesUseCase = mock(GetSupportedCurrenciesUseCase::class.java)
        val favoriteCurrenciesUseCase = mock(FavoriteCurrenciesUseCase::class.java)
        `when`(getSupportedCurrenciesUseCase(true)).thenReturn(listCurrencies)
        `when`(favoriteCurrenciesUseCase.get()).thenReturn(listOf())
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase, favoriteCurrenciesUseCase
        )
        for (item in listCurrencyItemsAllFalse) {
            currencyChoiceDialogViewModel.onItemSelected(item, true)
        }

        currencyChoiceDialogViewModel.items.test {
            assertEquals(listCurrencyItemsAllTrue, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun testRemoveItems(): Unit = runBlocking {
        val getSupportedCurrenciesUseCase = mock(GetSupportedCurrenciesUseCase::class.java)
        val favoriteCurrenciesUseCase = mock(FavoriteCurrenciesUseCase::class.java)
        `when`(getSupportedCurrenciesUseCase(true)).thenReturn(listCurrencies)
        `when`(favoriteCurrenciesUseCase.get()).thenReturn(listCurrencies)
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase, favoriteCurrenciesUseCase
        )
        verify(favoriteCurrenciesUseCase).get()

        currencyChoiceDialogViewModel.onItemSelected(
            BaseCurrencyChoiceDialogViewModel.SelectableItem(
                title = "AFN",
                subtitle = "4",
                isSelected = true
            ), false
        )
        currencyChoiceDialogViewModel.onItemSelected(
            BaseCurrencyChoiceDialogViewModel.SelectableItem(
                title = "ALL",
                subtitle = "2",
                isSelected = true
            ), false
        )

        val expectedList = listCurrencyItemsAllTrue.toMutableList()
        expectedList
            .filter { it.title == "AFN" || it.title == "ALL" }
            .map { it.isSelected = false }

        currencyChoiceDialogViewModel.items.test {
            assertEquals(expectedList, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun testLessThenTwoItemsAreSelected(): Unit = runBlocking {
        val getSupportedCurrenciesUseCase = mock(GetSupportedCurrenciesUseCase::class.java)
        val favoriteCurrenciesUseCase = mock(FavoriteCurrenciesUseCase::class.java)
        `when`(getSupportedCurrenciesUseCase(true)).thenReturn(listCurrencies)
        `when`(favoriteCurrenciesUseCase.get()).thenReturn(listOf())
        `when`(favoriteCurrenciesUseCase.set(setOf("AMD"))).thenThrow(IllegalFavoritesCountException())
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase, favoriteCurrenciesUseCase
        )
        currencyChoiceDialogViewModel.onItemSelected(
            BaseCurrencyChoiceDialogViewModel.SelectableItem(
                title = "AMD",
                subtitle = "1",
                isSelected = false
            ), true
        )

        currencyChoiceDialogViewModel.errors.test {
            currencyChoiceDialogViewModel.onOkClick {}

            assertEquals(R.string.not_enough_changed_currency, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun testSavingItems(): Unit = runBlocking {
        val getSupportedCurrenciesUseCase = mock(GetSupportedCurrenciesUseCase::class.java)
        val favoriteCurrenciesUseCase = mock(FavoriteCurrenciesUseCase::class.java)
        `when`(getSupportedCurrenciesUseCase(true)).thenReturn(listCurrencies)
        `when`(favoriteCurrenciesUseCase.get()).thenReturn(listOf())
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase, favoriteCurrenciesUseCase
        )
        for (item in listCurrencyItemsAllFalse) {
            currencyChoiceDialogViewModel.onItemSelected(item, true)
        }

        currencyChoiceDialogViewModel.onOkClick {}

        val expectedList = setOf("AMD", "ALL", "AED", "AFN")
        verify(favoriteCurrenciesUseCase).set(expectedList)
    }

    @Test
    fun testLoaderOnOkClick(): Unit = runBlocking {
        val getSupportedCurrenciesUseCase = mock(GetSupportedCurrenciesUseCase::class.java)
        val favoriteCurrenciesUseCase = mock(FavoriteCurrenciesUseCase::class.java)
        `when`(getSupportedCurrenciesUseCase(true)).thenReturn(listCurrencies)
        `when`(favoriteCurrenciesUseCase.get()).thenReturn(listCurrencies)
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            getSupportedCurrenciesUseCase, favoriteCurrenciesUseCase
        )
        currencyChoiceDialogViewModel.onItemSelected(
            BaseCurrencyChoiceDialogViewModel.SelectableItem(
                title = "ALL",
                subtitle = "2",
                isSelected = true
            ), false
        )

        currencyChoiceDialogViewModel.isLoading.test {
            assertEquals(false, awaitItem())

            currencyChoiceDialogViewModel.onOkClick {}

            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
        }
    }

}