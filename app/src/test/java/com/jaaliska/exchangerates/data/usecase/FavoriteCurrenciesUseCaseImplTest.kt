package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.const.TestData.listCurrencies
import com.jaaliska.exchangerates.const.TestData.setCodes
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.IllegalFavoritesCountException
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import com.jaaliska.exchangerates.utils.assertThrows
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FavoriteCurrenciesUseCaseImplTest : BaseTestCase() {

    @Test
    fun testSetHappyPath() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyRepository::class.java)
        val refreshRatesUseCase = mock(RefreshRatesUseCase::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val favoriteCurrenciesUseCaseImpl =
            FavoriteCurrenciesUseCaseImpl(
                localCurrencyRepository,
                refreshRatesUseCase,
                preferencesRepository
            )
        Mockito.`when`(preferencesRepository.getBaseCurrencyCode()).thenReturn("AMD")

        favoriteCurrenciesUseCaseImpl.set(setCodes)

        verify(localCurrencyRepository).saveFavoriteCurrencies(setCodes)
        verify(refreshRatesUseCase).invoke()
    }

    @Test
    fun `when base currency code absent`() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyRepository::class.java)
        val refreshRatesUseCase = mock(RefreshRatesUseCase::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val favoriteCurrenciesUseCaseImpl =
            FavoriteCurrenciesUseCaseImpl(
                localCurrencyRepository,
                refreshRatesUseCase,
                preferencesRepository
            )
        Mockito.`when`(preferencesRepository.getBaseCurrencyCode()).thenReturn("ADDS")

        favoriteCurrenciesUseCaseImpl.set(setCodes)

        verify(localCurrencyRepository).saveFavoriteCurrencies(setCodes)
        verify(preferencesRepository).setBaseCurrencyCode("AMD")
        verify(refreshRatesUseCase).invoke()
    }

    @Test
    fun testSetThrowsIllegalFavoritesCountException() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyRepository::class.java)
        val refreshRatesUseCase = mock(RefreshRatesUseCase::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val favoriteCurrenciesUseCaseImpl =
            FavoriteCurrenciesUseCaseImpl(
                localCurrencyRepository,
                refreshRatesUseCase,
                preferencesRepository
            )

        assertThrows<IllegalFavoritesCountException> {
            favoriteCurrenciesUseCaseImpl.set(setOf("AMD"))
        }
    }

    @Test
    fun testGet() = runBlockingTest {
        val localCurrencyRepository = mock(RoomCurrencyRepository::class.java)
        val refreshRatesUseCase = mock(RefreshRatesUseCase::class.java)
        val preferencesRepository = mock(PreferencesRepository::class.java)
        val favoriteCurrenciesUseCaseImpl =
            FavoriteCurrenciesUseCaseImpl(
                localCurrencyRepository,
                refreshRatesUseCase,
                preferencesRepository
            )
        Mockito.`when`(localCurrencyRepository.readFavoriteCurrencies()).thenReturn(listCurrencies)

        val result = favoriteCurrenciesUseCaseImpl.get()

        assertEquals(listCurrencies, result)
    }

}