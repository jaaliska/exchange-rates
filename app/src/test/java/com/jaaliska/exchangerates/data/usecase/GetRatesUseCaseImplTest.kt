package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.const.TestData.exchangeRatesByAMD
import com.jaaliska.exchangerates.const.TestData.listCurrencies
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.repository.RoomRatesRepository
import com.jaaliska.exchangerates.domain.RatesNotFoundException
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import kotlinx.coroutines.*
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals

class GetRatesUseCaseImplTest : BaseTestCase() {

    private val localRatesRepository = Mockito.mock(RoomRatesRepository::class.java)
    private val localCurrencyRepository = Mockito.mock(RoomCurrencyRepository::class.java)
    private val refreshRatesUseCase = Mockito.mock(RefreshRatesUseCase::class.java)
    private val getRatesUseCaseImpl =
        GetRatesUseCaseImpl(
            localRatesRepository, localCurrencyRepository, refreshRatesUseCase
        )



    @Test
    fun testInvokeHappyPath() = runBlocking {
        Mockito.`when`(localRatesRepository.getRates("AMD", listCurrencies))
            .thenReturn(exchangeRatesByAMD)

        val result = getRatesUseCaseImpl("AMD", listCurrencies)
        assertEquals(exchangeRatesByAMD, result)
    }

    @Test
    fun `when currencies absent`() = runBlocking {
        Mockito.`when`(localCurrencyRepository.readFavoriteCurrencies()).thenReturn(listCurrencies)
        Mockito.`when`(localRatesRepository.getRates("AMD", listCurrencies))
            .thenReturn(exchangeRatesByAMD)

        val result = getRatesUseCaseImpl("AMD")
        assertEquals(exchangeRatesByAMD, result)
    }

    @Test
    fun `when throw RatesNotFoundException`() = runBlocking { //TODO
        Mockito.`when`(localRatesRepository.getRates("AMD", listCurrencies)).thenThrow(
            RatesNotFoundException::class.java
        )
        getRatesUseCaseImpl("AMD", listCurrencies)
        Mockito.verify(refreshRatesUseCase).invoke()
    }
}