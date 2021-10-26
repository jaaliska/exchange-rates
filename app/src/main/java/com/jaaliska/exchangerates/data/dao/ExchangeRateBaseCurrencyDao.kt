package com.jaaliska.exchangerates.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jaaliska.exchangerates.data.model.database.RoomExchangeRateBaseCurrency

interface ExchangeRateBaseCurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: RoomExchangeRateBaseCurrency)

    @Query("DELETE FROM exchange_rate_base_currency")
    suspend fun deleteAll()

    @Query("SELECT * FROM exchange_rate_base_currency WHERE currency_code = :currencyCode")
    suspend fun getByCurrencyCode(currencyCode: String): RoomExchangeRateBaseCurrency?

}