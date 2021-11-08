package com.jaaliska.exchangerates.data.rates.dao

import com.jaaliska.exchangerates.domain.model.Rate
import kotlinx.coroutines.flow.map

class RoomRatesRepository(private val ratesDao: RateDao) {

    fun readAll() = ratesDao.readAll().map { roomRates -> roomRates.map(RoomRateWithCurrency::toDomain) }

    suspend fun deleteAll() = ratesDao.deleteAll()
    suspend fun saveAll(rates: List<Rate>) = ratesDao.insertAll(rates.map(::RoomRate))
}