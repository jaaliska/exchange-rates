package com.jaaliska.exchangerates.presentation.ui.currencyChoice

import app.cash.turbine.test
import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.const.TestData.listCurrencies
import com.jaaliska.exchangerates.const.TestData.setOfCurrencyCodes
import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import com.jaaliska.exchangerates.domain.usecases.SetFavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.presentation.mapping.toSelectableItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CurrencyChoiceDialogViewModelTest : BaseTestCase() {

    @Test
    fun `test selecting item is modifying list of items`(): Unit = runBlockingTest {
        val initialFavorites = listOf<Currency>()
        val initialSupportedCurrencies = listCurrencies
        val initialItems = listCurrencies.map { currency ->
            currency.toSelectableItem(false)
        }
        val expectedItems = listCurrencies.map { currency ->
            currency.toSelectableItem(true)
        }

        val setFavoriteCurrenciesUseCase = mock(SetFavoriteCurrenciesUseCase::class.java)
        val currencies = mock(CurrenciesDataSource::class.java)
        `when`(currencies.getSupported(true)).thenReturn(initialSupportedCurrencies)
        `when`(currencies.getFavorite()).thenReturn(initialFavorites)
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            setFavoriteCurrenciesUseCase, currencies
        )

        for (item in initialItems) {
            currencyChoiceDialogViewModel.onItemSelected(item, true)
        }

        currencyChoiceDialogViewModel.items.test {
            assertEquals(expectedItems, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `test deselecting item is modifying list of items`(): Unit = runBlockingTest {
        val initialFavorites = listCurrencies
        val initialSupportedCurrencies = listCurrencies
        val initialItems = listCurrencies.map { currency ->
            currency.toSelectableItem(true)
        }
        val expectedItems = initialItems.mapIndexed { index, item ->
            item.copy(isSelected = index >= 2)
        }

        val setFavoriteCurrenciesUseCase = mock(SetFavoriteCurrenciesUseCase::class.java)
        val currencies = mock(CurrenciesDataSource::class.java)
        `when`(currencies.getSupported(true)).thenReturn(initialSupportedCurrencies)
        `when`(currencies.getFavorite()).thenReturn(initialFavorites)
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            setFavoriteCurrenciesUseCase, currencies
        )

        for (item in initialItems.subList(0, 2)) {
            currencyChoiceDialogViewModel.onItemSelected(item, false)
        }

        currencyChoiceDialogViewModel.items.test {
            assertEquals(expectedItems, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `test less then two items are selected`(): Unit = runBlockingTest {
        val initialSupportedCurrencies = listCurrencies
        val selectableItem = listCurrencies.first().toSelectableItem(false)

        val setFavoriteCurrenciesUseCase = mock(SetFavoriteCurrenciesUseCase::class.java)
        val currencies = mock(CurrenciesDataSource::class.java)
        `when`(currencies.getSupported(true)).thenReturn(initialSupportedCurrencies)
        `when`(currencies.getFavorite()).thenReturn(listOf())
        `when`(setFavoriteCurrenciesUseCase.invoke(setOf(selectableItem.title))).thenThrow(
            IllegalFavoritesCountException()
        )
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            setFavoriteCurrenciesUseCase, currencies
        )
        currencyChoiceDialogViewModel.onItemSelected(selectableItem, true)

        currencyChoiceDialogViewModel.error.test {
            currencyChoiceDialogViewModel.submitItems {}

            assertEquals(R.string.not_enough_changed_currency, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `test saving items`(): Unit = runBlockingTest {
        val initialSupportedCurrencies = listCurrencies
        val selectableItems = listCurrencies.map { currency ->
            currency.toSelectableItem(false)
        }
        val expectedList = setOfCurrencyCodes

        val favoriteCurrenciesUseCase = mock(SetFavoriteCurrenciesUseCase::class.java)
        val currencies = mock(CurrenciesDataSource::class.java)
        `when`(currencies.getSupported(true)).thenReturn(initialSupportedCurrencies)
        `when`(currencies.getFavorite()).thenReturn(listOf())
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            favoriteCurrenciesUseCase, currencies
        )
        for (item in selectableItems) {
            currencyChoiceDialogViewModel.onItemSelected(item, true)
        }

        currencyChoiceDialogViewModel.submitItems {}

        verify(favoriteCurrenciesUseCase).invoke(expectedList)
    }

    @Test
    fun `test loader submit items`(): Unit = runBlockingTest {
        val initialFavorites = listCurrencies
        val initialSupportedCurrencies = listCurrencies
        val selectableItem = listCurrencies.last().toSelectableItem(true)

        val favoriteCurrenciesUseCase = mock(SetFavoriteCurrenciesUseCase::class.java)
        val currencies = mock(CurrenciesDataSource::class.java)
        `when`(currencies.getSupported(true)).thenReturn(initialSupportedCurrencies)
        `when`(currencies.getFavorite()).thenReturn(initialFavorites)
        val currencyChoiceDialogViewModel = CurrencyChoiceDialogViewModel(
            favoriteCurrenciesUseCase, currencies
        )
        currencyChoiceDialogViewModel.onItemSelected(selectableItem, false)

        currencyChoiceDialogViewModel.isLoading.test {
            assertEquals(false, awaitItem())

            currencyChoiceDialogViewModel.submitItems {}

            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
        }
    }

}