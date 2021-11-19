package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.const.TestData
import com.jaaliska.exchangerates.const.TestData.exchangeRatesByAED
import com.jaaliska.exchangerates.const.TestData.exchangeRatesByAFN
import com.jaaliska.exchangerates.const.TestData.exchangeRatesByALL
import com.jaaliska.exchangerates.const.TestData.listCurrenciesWithoutBaseCurrency
import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.repository.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.rates.repository.RoomRatesRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.presentation.service.AlarmService
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

class RefreshRatesUseCaseImplTest : TestCase() {

    private val localRatesRepository = Mockito.mock(RoomRatesRepository::class.java)
    private val localCurrencyRepository = Mockito.mock(RoomCurrencyRepository::class.java)
    private val remoteRatesRepository = Mockito.mock(RetrofitRatesRepository::class.java)
    private val alarmService = Mockito.mock(AlarmService::class.java)
    private val refreshRatesUseCaseImpl =
        RefreshRatesUseCaseImpl(
            localRatesRepository, remoteRatesRepository, localCurrencyRepository, alarmService
        )

    @Test
    fun testInvokeHappyPath() = runBlocking {
        Mockito.`when`(localCurrencyRepository.readFavoriteCurrencies())
            .thenReturn(TestData.listCurrencies)
        Mockito.`when`(
            remoteRatesRepository.getRates(
                Currency("AED", "United Arab Emirates dirham"),
                listCurrenciesWithoutBaseCurrency
            )
        )
            .thenReturn(TestData.exchangeRatesByAED)
        Mockito.`when`(
            remoteRatesRepository.getRates(
                Currency("AFN", "Afghan afghani"),
                listCurrenciesWithoutBaseCurrency
            )
        )
            .thenReturn(TestData.exchangeRatesByAFN)
        Mockito.`when`(
            remoteRatesRepository.getRates(
                Currency("ALL", "Albanian lek"),
                listCurrenciesWithoutBaseCurrency
            )
        )
            .thenReturn(TestData.exchangeRatesByALL)


        Mockito.`when`(localRatesRepository.getRates("AMD", TestData.listCurrencies))
            .thenReturn(TestData.exchangeRatesByAMD)

        refreshRatesUseCaseImpl()
        Mockito.verify(localRatesRepository).deleteAllRates()
        Mockito.verify(localRatesRepository).saveRates(exchangeRatesByAED)
        Mockito.verify(localRatesRepository).saveRates(exchangeRatesByAFN)
        Mockito.verify(localRatesRepository).saveRates(exchangeRatesByALL)
        Mockito.verify(alarmService).startAlarm()
    }

}