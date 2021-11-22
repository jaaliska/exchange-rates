package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.const.TestData.setCodes
import com.jaaliska.exchangerates.const.TestData.listCurrencies
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import kotlinx.coroutines.*
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FavoriteCurrenciesUseCaseImplTest : BaseTestCase() {

    private val localCurrencyRepository = mock(RoomCurrencyRepository::class.java)
    private val refreshRatesUseCase = mock(RefreshRatesUseCase::class.java)
    private val preferencesRepository = mock(PreferencesRepository::class.java)
    private val favoriteCurrenciesUseCaseImpl =
        FavoriteCurrenciesUseCaseImpl(
            localCurrencyRepository, refreshRatesUseCase, preferencesRepository
        )

    @Test
    fun testSetHappyPath() = runBlocking {
        Mockito.`when`(preferencesRepository.getBaseCurrencyCode()).thenReturn("AMD")
        favoriteCurrenciesUseCaseImpl.set(setCodes)
        verify(localCurrencyRepository).saveFavoriteCurrencies(setCodes)
        verify(refreshRatesUseCase).invoke()
    }

    @Test
    fun `when base currency code absent`() = runBlocking {
        Mockito.`when`(preferencesRepository.getBaseCurrencyCode()).thenReturn("ADDS")
        favoriteCurrenciesUseCaseImpl.set(setCodes)
        verify(localCurrencyRepository).saveFavoriteCurrencies(setCodes)
        verify(preferencesRepository).setBaseCurrencyCode("AMD")
        verify(refreshRatesUseCase).invoke()
    }

    @Test
    fun testGet() = runBlocking {
        Mockito.`when`(localCurrencyRepository.readFavoriteCurrencies()).thenReturn(listCurrencies)
        val result = favoriteCurrenciesUseCaseImpl.get()
        assertEquals(listCurrencies, result)
    }

}