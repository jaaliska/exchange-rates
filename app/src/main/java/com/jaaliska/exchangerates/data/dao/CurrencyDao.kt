package com.jaaliska.exchangerates.data.dao

import androidx.room.*
import com.jaaliska.exchangerates.data.model.database.RoomCurrency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: List<RoomCurrency>)

    @Query("UPDATE currency SET isFavorite = code IN (:codes)")
    suspend fun setIsFavorite(codes: List<String>)

    @Query("DELETE FROM currency")
    suspend fun deleteAll()

    @Query("SELECT * FROM currency WHERE code IN (:codes)")
    suspend fun getByCodes(codes: List<String>): List<RoomCurrency>

    @Query("SELECT * FROM currency")
    suspend fun readSupportedCurrency(): List<RoomCurrency>

    @Query("SELECT code FROM currency WHERE isFavorite")
    suspend fun readFavoriteCurrencyCodes(): List<String>
}