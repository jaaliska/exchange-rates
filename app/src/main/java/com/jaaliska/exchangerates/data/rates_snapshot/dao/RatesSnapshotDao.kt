package com.jaaliska.exchangerates.data.rates_snapshot.dao

import androidx.room.*
import com.jaaliska.exchangerates.data.rates_snapshot.dao.model.rate.RoomRate
import com.jaaliska.exchangerates.data.rates_snapshot.dao.model.snapshot.RoomRatesSnapshot
import com.jaaliska.exchangerates.data.rates_snapshot.dao.model.snapshot.RoomRatesSnapshotWithRates
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RatesSnapshotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(snapshot: RoomRatesSnapshot)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(rate: RoomRate)

    @Query("DELETE FROM rates_snapshot WHERE base_currency_code = :baseCurrencyCode")
    abstract suspend fun delete(baseCurrencyCode: String)

    @Transaction
    @Query("SELECT * FROM rates_snapshot WHERE base_currency_code = :baseCurrencyCode")
    abstract fun read(baseCurrencyCode: String): Flow<RoomRatesSnapshotWithRates?>


    @Transaction
    open suspend fun insert(snapshot: RoomRatesSnapshotWithRates) {
        insert(snapshot.ratesSnapshot)
        snapshot.rates.forEach { insert(it.rate) }
    }
}