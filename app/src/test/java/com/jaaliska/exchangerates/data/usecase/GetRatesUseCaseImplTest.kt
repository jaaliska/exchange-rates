package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.const.TestData.exchangeRatesByAMD
import com.jaaliska.exchangerates.const.TestData.listCurrencies
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.repository.RoomRatesRepository
import com.jaaliska.exchangerates.domain.RatesNotFoundException
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetRatesUseCaseImplTest : BaseTestCase() {

    @Test
    fun testInvokeHappyPath() = runBlockingTest  {
        val localRatesRepository = Mockito.mock(RoomRatesRepository::class.java)
        val localCurrencyRepository = Mockito.mock(RoomCurrencyRepository::class.java)
        val refreshRatesUseCase = Mockito.mock(RefreshRatesUseCase::class.java)
        val getRatesUseCaseImpl =
            GetRatesUseCaseImpl(
                localRatesRepository, localCurrencyRepository, refreshRatesUseCase
            )
        Mockito.`when`(localRatesRepository.getRates("AMD", listCurrencies))
            .thenReturn(exchangeRatesByAMD)

        val result = getRatesUseCaseImpl("AMD", listCurrencies)

        assertEquals(exchangeRatesByAMD, result)
    }

    @Test
    fun `when currencies absent`() = runBlockingTest  {
        val localRatesRepository = Mockito.mock(RoomRatesRepository::class.java)
        val localCurrencyRepository = Mockito.mock(RoomCurrencyRepository::class.java)
        val refreshRatesUseCase = Mockito.mock(RefreshRatesUseCase::class.java)
        val getRatesUseCaseImpl =
            GetRatesUseCaseImpl(
                localRatesRepository, localCurrencyRepository, refreshRatesUseCase
            )
        Mockito.`when`(localCurrencyRepository.readFavoriteCurrencies()).thenReturn(listCurrencies)
        Mockito.`when`(localRatesRepository.getRates("AMD", listCurrencies))
            .thenReturn(exchangeRatesByAMD)

        val result = getRatesUseCaseImpl("AMD")

        assertEquals(exchangeRatesByAMD, result)
    }

    @Test
    fun `when throw RatesNotFoundException`(): Unit = runBlockingTest  {
        val localRatesRepository = Mockito.mock(RoomRatesRepository::class.java)
        val localCurrencyRepository = Mockito.mock(RoomCurrencyRepository::class.java)
        val refreshRatesUseCase = Mockito.mock(RefreshRatesUseCase::class.java)
        val getRatesUseCaseImpl =
            GetRatesUseCaseImpl(
                localRatesRepository, localCurrencyRepository, refreshRatesUseCase
            )
        Mockito.`when`(localRatesRepository.getRates("AMD", listCurrencies))
            .thenThrow(RatesNotFoundException("AMD"))
            .thenReturn(exchangeRatesByAMD)

        getRatesUseCaseImpl("AMD", listCurrencies)

        Mockito.verify(refreshRatesUseCase).invoke()
        Mockito.verify(localRatesRepository, times(2))
            .getRates("AMD", listCurrencies)
    }
}