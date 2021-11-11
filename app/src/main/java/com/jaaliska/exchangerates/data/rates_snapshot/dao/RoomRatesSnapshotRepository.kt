package com.jaaliska.exchangerates.data.rates_snapshot.dao

import com.jaaliska.exchangerates.data.rates_snapshot.dao.model.snapshot.RoomRatesSnapshotWithRates
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.model.RatesSnapshot
import kotlinx.coroutines.flow.map

class RoomRatesSnapshotRepository(
    private val ratesSnapshotDao: RatesSnapshotDao
) {

    fun read(baseCurrency: Currency) = ratesSnapshotDao.read(baseCurrencyCode = baseCurrency.code)
        .map { roomRates -> roomRates?.toDomain() }

    suspend fun delete(baseCurrency: Currency) =
        ratesSnapshotDao.delete(baseCurrencyCode = baseCurrency.code)

    suspend fun save(ratesSnapshot: RatesSnapshot) {
        ratesSnapshotDao.insert(RoomRatesSnapshotWithRates(ratesSnapshot))
    }
}