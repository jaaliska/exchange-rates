package com.jaaliska.exchangerates.tests.presentation

import app.cash.turbine.test
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.core.BaseUnitTest
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecase.UpdateCurrencyFavoriteStateUseCase
import com.jaaliska.exchangerates.presentation.ui.common.list.checkable_item.CheckableItem
import com.jaaliska.exchangerates.presentation.ui.screens.currency_choice.BaseCurrencyChoiceViewModel
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
        // Given
        val allCurrencies = listOf(
            Currency(name = "1", code = "1"),
            Currency(name = "2", code = "2")
        )
        val favoriteCurrencies = listOf(Currency(name = "1", code = "1"))

        val expectedState = BaseCurrencyChoiceViewModel.State(
            items = allCurrencies.map { currency ->
                currency.toCheckableItem(
                    isChecked = favoriteCurrencies.any { it.code == currency.code })
            },
            isLoading = false,
            error = null
        )

        val allCurrenciesFlow = flowOf(allCurrencies)
        val favoriteCurrenciesFlow = flowOf(favoriteCurrencies)

        val useCase = mock<UpdateCurrencyFavoriteStateUseCase>()

        val dataSource = mock<CurrenciesDataSource> {
            on { observeAll() }.then { allCurrenciesFlow }
            on { observeFavorites() }.then { favoriteCurrenciesFlow }
        }

        // When
        val viewModel = CurrencyChoiceDialogViewModel(
            currenciesDataSource = dataSource,
            updateCurrencyFavoriteStateUseCase = useCase,
            ioDispatcher = dispatcher
        )

        // Then
        viewModel.state.test {
            assertEquals(expectedState, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `test initial failure load`() = runBlockingTest {
        // Given
        val useCase = mock<UpdateCurrencyFavoriteStateUseCase>()

        val dataSource = mock<CurrenciesDataSource> {
            on { observeAll() }.then { flow<List<Currency>> { throw RuntimeException() } }
            on { observeFavorites() }.then { flow<List<Currency>> { throw RuntimeException() } }
        }

        val expectedState = BaseCurrencyChoiceViewModel.State(
            items = listOf(),
            isLoading = false,
            error = R.string.something_went_wrong
        )

        // When
        val viewModel = CurrencyChoiceDialogViewModel(
            currenciesDataSource = dataSource,
            updateCurrencyFavoriteStateUseCase = useCase,
            ioDispatcher = dispatcher
        )

        // Then
        viewModel.state.test {
            assertEquals(awaitItem(), expectedState)
            expectNoEvents()
        }
    }

    @Test
    fun `show loading when item selected or deselected`() = runBlockingTest {
        // Given
        val initialItem = CheckableItem("1", "2", isChecked = true)

        val useCase = mock<UpdateCurrencyFavoriteStateUseCase>()

        val dataSource = mock<CurrenciesDataSource>()

        val viewModel = CurrencyChoiceDialogViewModel(
            currenciesDataSource = dataSource,
            updateCurrencyFavoriteStateUseCase = useCase,
            ioDispatcher = dispatcher
        )

        viewModel.state.test {
            // When
            viewModel.onItemClick(initialItem, true)

            // Then
            assertEquals(false, awaitItem().isLoading)
            assertEquals(true, awaitItem().isLoading)
            assertEquals(false, awaitItem().isLoading)

            expectNoEvents()
        }
    }

    @Test
    fun `deselect currency should update items`() = runBlockingTest {
        // Given
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

        val expectedState = BaseCurrencyChoiceViewModel.State(
            items = allCurrencies.map { it.toCheckableItem(false) },
            isLoading = false,
            error = null
        )

        // When
        viewModel.onItemClick(allCurrencies.first().toCheckableItem(true), false)

        viewModel.state.test {
            // Then
            assertEquals(expectedState, awaitItem())
            expectNoEvents()
        }
    }
}
