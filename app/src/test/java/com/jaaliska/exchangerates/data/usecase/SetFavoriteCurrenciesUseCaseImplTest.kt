package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.const.TestData.setOfCurrencyCodes
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.utils.assertThrows
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class SetFavoriteCurrenciesUseCaseImplTest : BaseTestCase() {

    @Test
    fun `test set favorite currencies successes`() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyRepository::class.java)
        val ratesDataSource = mock(RatesDataSource::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val setFavoriteCurrenciesUseCaseImpl =
            SetFavoriteCurrenciesUseCaseImpl(
                localCurrencyRepository,
                ratesDataSource,
                preferencesRepository
            )
        Mockito.`when`(preferencesRepository.getBaseCurrencyCode()).thenReturn(setOfCurrencyCodes.first())

        setFavoriteCurrenciesUseCaseImpl.invoke(setOfCurrencyCodes)

        verify(localCurrencyRepository).saveFavoriteCurrencies(setOfCurrencyCodes)
        verify(ratesDataSource).refresh()
    }

    @Test
    fun `when base currency code absent`() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyRepository::class.java)
        val ratesDataSource = mock(RatesDataSource::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val setFavoriteCurrenciesUseCaseImpl =
            SetFavoriteCurrenciesUseCaseImpl(
                localCurrencyRepository,
                ratesDataSource,
                preferencesRepository
            )
        Mockito.`when`(preferencesRepository.getBaseCurrencyCode()).thenReturn("Not exist code")

        setFavoriteCurrenciesUseCaseImpl.invoke(setOfCurrencyCodes)

        verify(localCurrencyRepository).saveFavoriteCurrencies(setOfCurrencyCodes)
        verify(preferencesRepository).setBaseCurrencyCode(setOfCurrencyCodes.first())
        verify(ratesDataSource).refresh()
    }

    @Test
    fun testSetThrowsIllegalFavoritesCountException() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyRepository::class.java)
        val ratesDataSource = mock(RatesDataSource::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val setFavoriteCurrenciesUseCaseImpl =
            SetFavoriteCurrenciesUseCaseImpl(
                localCurrencyRepository,
                ratesDataSource,
                preferencesRepository
            )

        assertThrows<IllegalFavoritesCountException> {
            setFavoriteCurrenciesUseCaseImpl.invoke(setOf(setOfCurrencyCodes.first()))
        }
    }

}