package com.jaaliska.exchangerates.tests.presentation

import app.cash.turbine.test
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.core.BaseUnitTest
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecase.UpdateCurrencyFavoriteStateUseCase
import com.jaaliska.exchangerates.presentation.ui.common.list.checkable_item.CheckableItem
import com.jaaliska.exchangerates.presentation.ui.screens.currency_choice.CurrencyChoiceDialogViewModel
import com.jaaliska.exchangerates.presentation.ui.screens.currency_choice.CurrencyChoiceDialogViewModel.Companion.toCheckableItem
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class CurrencyChoiceViewModelUnitTest : BaseUnitTest() {

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun `test initial success load`(): Unit = runBlockingTest {
        val allCurrencies = listOf(
            Currency(name = "1", code = "1"),
            Currency(name = "2", code = "2")
        )
        val favoriteCurrencies = listOf(Currency(name = "1", code = "1"))

        val expectedItems = allCurrencies.map { currency ->
            currency.toCheckableItem(
                isChecked = favoriteCurrencies.any { it.code == currency.code })
        }

        val allCurrenciesFlow = flowOf(allCurrencies)
        val favoriteCurrenciesFlow = flowOf(favoriteCurrencies)

        val useCase = mock<UpdateCurrencyFavoriteStateUseCase>()

        val dataSource = mock<CurrenciesDataSource> {
            on { observeAll() }.then { allCurrenciesFlow }
            on { observeFavorites() }.then { favoriteCurrenciesFlow }
        }

        val viewModel = CurrencyChoiceDialogViewModel(
            currenciesDataSource = dataSource,
            updateCurrencyFavoriteStateUseCase = useCase,
            ioDispatcher = testDispatcher
        )

        viewModel.items.test {
            assertEquals(expectedItems, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `test initial failure load`(): Unit = runBlockingTest {
        val useCase = mock<UpdateCurrencyFavoriteStateUseCase>()

        val dataSource = mock<CurrenciesDataSource> {
            on { observeAll() }.then { flow<List<Currency>> { throw RuntimeException() } }
            on { observeFavorites() }.then { flow<List<Currency>> { throw RuntimeException() } }
        }

        val viewModel = CurrencyChoiceDialogViewModel(
            currenciesDataSource = dataSource,
            updateCurrencyFavoriteStateUseCase = useCase,
            ioDispatcher = testDispatcher
        )

        viewModel.items.test {
            assertEquals(awaitItem(), listOf<CheckableItem>())
            expectNoEvents()
        }

        viewModel.error.test {
            assertEquals(awaitItem(), R.string.something_went_wrong)
            expectNoEvents()
        }
    }

    @Test
    fun `show loading when item selected or deselected`(): Unit = runBlockingTest {
        val initialItem = CheckableItem("1", "2", isChecked = true)

        val useCase = mock<UpdateCurrencyFavoriteStateUseCase>()

        val dataSource = mock<CurrenciesDataSource> {
            on { observeAll() }.then { flowOf<List<Currency>>(listOf()) }
            on { observeFavorites() }.then { flowOf<List<Currency>>(listOf()) }
        }

        val viewModel = CurrencyChoiceDialogViewModel(
            currenciesDataSource = dataSource,
            updateCurrencyFavoriteStateUseCase = useCase,
            ioDispatcher = testDispatcher
        )

        viewModel.isLoading.test {
            viewModel.onItemClick(initialItem, true)

            assertEquals(false, awaitItem())
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())

            expectNoEvents()
        }
    }
}
