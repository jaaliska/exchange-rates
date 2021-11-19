package com.jaaliska.exchangerates.tests.presentation

import app.cash.turbine.test
import com.jaaliska.exchangerates.app.di.app
import com.jaaliska.exchangerates.core.BaseUnitTest
import com.jaaliska.exchangerates.domain.datasource.CurrenciesDataSource
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecase.UpdateCurrencyFavoriteStateUseCase
import com.jaaliska.exchangerates.presentation.ui.screens.currency_choice.BaseCurrencyChoiceViewModel
import com.jaaliska.exchangerates.presentation.ui.screens.currency_choice.CurrencyChoiceDialogViewModel.Companion.toCheckableItem
import com.nhaarman.mockitokotlin2.given
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.koin.core.logger.Level
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.declareMock

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class CurrencyChoiceViewModelUnitTest : BaseUnitTest() {

    @get:Rule
    val injectDependenciesRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        modules(app)
    }


    private val viewModel by inject<BaseCurrencyChoiceViewModel>()


    @Test
    fun `test initial load`(): Unit = runBlocking {
        val allCurrencies =
            listOf(Currency(name = "1", code = "1"), Currency(name = "2", code = "2"))
        val favoriteCurrencies = listOf(Currency(name = "1", code = "1"))

        val expectedItems = allCurrencies.map { currency ->
            currency.toCheckableItem(
                isChecked = favoriteCurrencies.any { it.code == currency.code })
        }

        val allCurrenciesFlow = flowOf(allCurrencies)
        val favoriteCurrenciesFlow = flowOf(favoriteCurrencies)

        declareMock<CurrenciesDataSource> {
            given(observeAll()).willReturn(allCurrenciesFlow)
            given(observeFavorites()).willReturn(favoriteCurrenciesFlow)
        }

        declareMock<UpdateCurrencyFavoriteStateUseCase>()

        viewModel.items.test {
            assertEquals(expectedItems, awaitItem())
        }
    }
}
