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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class CurrencyChoiceViewModelUnitTest : BaseUnitTest(TestCoroutineDispatcher()) {

    @Test
    fun `test initial success load`() = runBlockingTest {
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
            ioDispatcher = dispatcher
        )

        viewModel.items.test {
            assertEquals(expectedItems, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `test initial failure load`() = runBlockingTest {
        val useCase = mock<UpdateCurrencyFavoriteStateUseCase>()

        val dataSource = mock<CurrenciesDataSource> {
            on { observeAll() }.then { flow<List<Currency>> { throw RuntimeException() } }
            on { observeFavorites() }.then { flow<List<Currency>> { throw RuntimeException() } }
        }

        val viewModel = CurrencyChoiceDialogViewModel(
            currenciesDataSource = dataSource,
            updateCurrencyFavoriteStateUseCase = useCase,
            ioDispatcher = dispatcher
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
    fun `show loading when item selected or deselected`() = runBlockingTest {
        val initialItem = CheckableItem("1", "2", isChecked = true)

        val useCase = mock<UpdateCurrencyFavoriteStateUseCase>()

        val dataSource = mock<CurrenciesDataSource>()

        val viewModel = CurrencyChoiceDialogViewModel(
            currenciesDataSource = dataSource,
            updateCurrencyFavoriteStateUseCase = useCase,
            ioDispatcher = dispatcher
        )

        viewModel.isLoading.test {
            viewModel.onItemClick(initialItem, true)

            assertEquals(false, awaitItem())
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())

            expectNoEvents()
        }
    }

    @Test
    fun `deselect currency should update items`() = runBlockingTest {
        // Arrange
        val allCurrencies = listOf(
            Currency(name = "1", code = "1"),
            Currency(name = "2", code = "2")
        )
        val favoriteCurrencies = mutableListOf(Currency(name = "1", code = "1"))

        val favoriteFlow = MutableStateFlow(favoriteCurrencies)
        val allCurrenciesFlow = MutableStateFlow(allCurrencies)

        val dataSource = object : CurrenciesDataSource {
            override fun observeAll() = allCurrenciesFlow
            override fun observeFavorites() = favoriteFlow
        }

        val useCase = object : UpdateCurrencyFavoriteStateUseCase {
            override suspend fun invoke(currency: Currency, isFavorite: Boolean) {
                favoriteFlow.emit(mutableListOf())
            }
        }

        val viewModel = CurrencyChoiceDialogViewModel(
            currenciesDataSource = dataSource,
            updateCurrencyFavoriteStateUseCase = useCase,
            ioDispatcher = Dispatchers.Unconfined
        )

        val expectedInitialItems =
            allCurrencies.map { currency -> currency.toCheckableItem(favoriteCurrencies.any { it.code == currency.code }) }

        val expectedUpdatedItems = allCurrencies.map { it.toCheckableItem(false) }

        viewModel.items.test {
            // Assert
            assertEquals(expectedInitialItems, awaitItem())
            // Act
            viewModel.onItemClick(allCurrencies.first().toCheckableItem(true), false)
            // Assert
            assertEquals(expectedUpdatedItems, awaitItem())
            expectNoEvents()
        }
    }
}
