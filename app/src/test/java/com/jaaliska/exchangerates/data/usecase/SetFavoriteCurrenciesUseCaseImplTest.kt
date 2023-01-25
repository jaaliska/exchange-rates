package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.const.TestData.setOfCurrencyCodes
import com.jaaliska.exchangerates.data.currency.datasource.RoomCurrencyDataSource
import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.repository.RatesRepository
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.SetFavoriteCurrenciesUseCase
import com.jaaliska.exchangerates.utils.assertThrows
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class SetFavoriteCurrenciesUseCaseTest : BaseTestCase() {

    @Test
    fun `test set favorite currencies successes`() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyDataSource::class.java)
        val ratesRepository = mock(RatesRepository::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val setFavoriteCurrenciesUseCase =
            SetFavoriteCurrenciesUseCase(
                localCurrencyRepository,
                ratesRepository,
                preferencesRepository
            )
        Mockito.`when`(preferencesRepository.getBaseCurrencyCode()).thenReturn(setOfCurrencyCodes.first())

        setFavoriteCurrenciesUseCase.invoke(setOfCurrencyCodes)

        verify(localCurrencyRepository).saveFavoriteCurrencies(setOfCurrencyCodes)
        verify(ratesRepository).refresh()
    }

    @Test
    fun `when base currency code absent`() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyDataSource::class.java)
        val ratesRepository = mock(RatesRepository::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val setFavoriteCurrenciesUseCase =
            SetFavoriteCurrenciesUseCase(
                localCurrencyRepository,
                ratesRepository,
                preferencesRepository
            )
        Mockito.`when`(preferencesRepository.getBaseCurrencyCode()).thenReturn("Not exist code")

        setFavoriteCurrenciesUseCase.invoke(setOfCurrencyCodes)

        verify(localCurrencyRepository).saveFavoriteCurrencies(setOfCurrencyCodes)
        verify(preferencesRepository).setBaseCurrencyCode(setOfCurrencyCodes.first())
        verify(ratesRepository).refresh()
    }

    @Test
    fun testSetThrowsIllegalFavoritesCountException() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyDataSource::class.java)
        val ratesRepository = mock(RatesRepository::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val setFavoriteCurrenciesUseCase =
            SetFavoriteCurrenciesUseCase(
                localCurrencyRepository,
                ratesRepository,
                preferencesRepository
            )

        assertThrows<IllegalFavoritesCountException> {
            setFavoriteCurrenciesUseCase.invoke(setOf(setOfCurrencyCodes.first()))
        }
    }

}