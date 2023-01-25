package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.BaseTestCase
import com.jaaliska.exchangerates.const.TestData.exchangeRatesByAMD
import com.jaaliska.exchangerates.const.TestData.listCurrencies
import com.jaaliska.exchangerates.data.currency.datasource.RoomCurrencyDataSource
import com.jaaliska.exchangerates.data.repository.RatesRepositoryImpl
import com.jaaliska.exchangerates.data.rates.datasource.RetrofitRatesDataSource
import com.jaaliska.exchangerates.data.rates.datasource.RoomRatesDataSource
import com.jaaliska.exchangerates.domain.RatesNotFoundException
import com.jaaliska.exchangerates.presentation.service.AlarmService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class RatesRepositoryImplTest : BaseTestCase() {

    @Test
    fun `test get rates`() = runBlockingTest  {
        val localRatesRepository = Mockito.mock(RoomRatesDataSource::class.java)
        val localCurrencyRepository = Mockito.mock(RoomCurrencyDataSource::class.java)
        val remoteRatesRepository = Mockito.mock(RetrofitRatesDataSource::class.java)
        val alarmService = Mockito.mock(AlarmService::class.java)
        val ratesRepositoryImpl =
            RatesRepositoryImpl(
                localRatesRepository, localCurrencyRepository, remoteRatesRepository, alarmService
            )
        Mockito.`when`(localRatesRepository.getRates("AMD", listCurrencies))
            .thenReturn(exchangeRatesByAMD)

        val result = ratesRepositoryImpl.get("AMD", listCurrencies)

        assertEquals(exchangeRatesByAMD, result)
    }

    @Test
    fun `test get rates when currencies absent`() = runBlockingTest  {
        val localRatesRepository = Mockito.mock(RoomRatesDataSource::class.java)
        val localCurrencyRepository = Mockito.mock(RoomCurrencyDataSource::class.java)
        val remoteRatesRepository = Mockito.mock(RetrofitRatesDataSource::class.java)
        val alarmService = Mockito.mock(AlarmService::class.java)
        val ratesRepositoryImpl =
            RatesRepositoryImpl(
                localRatesRepository, localCurrencyRepository, remoteRatesRepository, alarmService
            )
        Mockito.`when`(localCurrencyRepository.readFavoriteCurrencies()).thenReturn(listCurrencies)
        Mockito.`when`(localRatesRepository.getRates("AMD", listCurrencies))
            .thenReturn(exchangeRatesByAMD)

        val result = ratesRepositoryImpl.get("AMD")

        assertEquals(exchangeRatesByAMD, result)
    }

    @Test
    fun `test get rates when throw RatesNotFoundException`(): Unit = runBlockingTest  {
        val localRatesRepository = Mockito.mock(RoomRatesDataSource::class.java)
        val localCurrencyRepository = Mockito.mock(RoomCurrencyDataSource::class.java)
        val remoteRatesRepository = Mockito.mock(RetrofitRatesDataSource::class.java)
        val alarmService = Mockito.mock(AlarmService::class.java)
        val ratesRepositoryImpl =
            RatesRepositoryImpl(
                localRatesRepository, localCurrencyRepository, remoteRatesRepository, alarmService
            )
        Mockito.`when`(localCurrencyRepository.readFavoriteCurrencies()).thenReturn(listCurrencies)
        Mockito.`when`(localRatesRepository.getRates(listCurrencies.first().code, listCurrencies))
            .thenThrow(RatesNotFoundException(listCurrencies.first().code))
            .thenReturn(exchangeRatesByAMD)

        ratesRepositoryImpl.get(listCurrencies.first().code, listCurrencies)

        Mockito.verify(localRatesRepository, times(2))
            .getRates(listCurrencies.first().code, listCurrencies)
    }

    //TODO getNamedRates and refresh tests
}