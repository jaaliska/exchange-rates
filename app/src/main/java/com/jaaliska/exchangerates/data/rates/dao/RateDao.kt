package com.jaaliska.exchangerates.data.rates.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<RoomRate>)

    @Query("DELETE FROM rates")
    suspend fun deleteAll()

    @Query("SELECT * FROM rates")
    fun readAll(): Flow<List<RoomRateWithCurrency>>
}