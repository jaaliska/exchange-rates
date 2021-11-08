package com.jaaliska.exchangerates.data.currency.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: List<RoomCurrency>)

    @Query("UPDATE currency SET is_favorite = :isFavorite WHERE code = :code")
    suspend fun setIsFavorite(code: String, isFavorite: Boolean)

    @Query("DELETE FROM currency")
    suspend fun deleteAll()

    @Query("SELECT * FROM currency")
    fun readAll(): Flow<List<RoomCurrency>>

    @Query("SELECT * FROM currency WHERE is_favorite")
    fun readFavorites(): Flow<List<RoomCurrency>>
}