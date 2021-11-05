package com.jaaliska.exchangerates.data.rates.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRateBaseCurrency
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateBaseCurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: RoomExchangeRateBaseCurrency)

    @Query("DELETE FROM exchange_rate_base_currency")
    suspend fun deleteAll()

    @Query("SELECT * FROM exchange_rate_base_currency WHERE currency_code = :currencyCode")
    suspend fun getByCurrencyCode(currencyCode: String): RoomExchangeRateBaseCurrency?

    @Query("SELECT * FROM exchange_rate_base_currency")
    fun getAll(): Flow<List<RoomExchangeRateBaseCurrency>>

}