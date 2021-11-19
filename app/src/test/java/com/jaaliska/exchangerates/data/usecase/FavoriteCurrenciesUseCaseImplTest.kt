package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.const.TestData.listCodes
import com.jaaliska.exchangerates.const.TestData.listCurrencies
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.internal.matchers.ArrayEquals
import org.mockito.stubbing.Answer
import java.lang.Exception
import kotlin.test.assertEquals

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
        Mockito.`when`(preferencesRepository.setBaseCurrencyCode("AMD")).thenThrow(FalseCallException::class.java)
        favoriteCurrenciesUseCaseImpl.set(listCodes)
        verify(localCurrencyRepository).saveFavoriteCurrencies(listCodes)
        verify(refreshRatesUseCase).invoke()
    }

    @Test
    fun `when base currency code absent`() = runBlocking {
        Mockito.`when`(preferencesRepository.getBaseCurrencyCode()).thenReturn("ADDS")
        favoriteCurrenciesUseCaseImpl.set(listCodes)
        verify(localCurrencyRepository).saveFavoriteCurrencies(listCodes)
        verify(preferencesRepository).setBaseCurrencyCode("AMD")
        verify(refreshRatesUseCase).invoke()
    }

    @Test
    fun testGet() = runBlocking {
        Mockito.`when`(localCurrencyRepository.readFavoriteCurrencies()).thenReturn(listCurrencies)
        val result = favoriteCurrenciesUseCaseImpl.get()
        assertEquals(listCurrencies, result)
    }

    companion object {
        class FalseCallException: NullPointerException()//TODO investigate
    }
}