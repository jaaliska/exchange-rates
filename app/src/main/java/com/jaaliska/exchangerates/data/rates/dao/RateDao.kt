package com.jaaliska.exchangerates.data.rates.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRates
import kotlinx.coroutines.flow.Flow

@Dao
interface RateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: List<RoomExchangeRates>)

    @Query("DELETE FROM rates")
    suspend fun deleteAll()

    @Query("SELECT * FROM rates WHERE base_code = :baseCode AND code IN (:currencyCodes)")
    suspend fun getByBaseCode(baseCode: String, currencyCodes: List<String>): List<RoomExchangeRates>

}