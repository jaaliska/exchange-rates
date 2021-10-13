package com.jaaliska.exchangerates.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jaaliska.exchangerates.data.model.database.RateEntity

@Dao
interface RateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: List<RateEntity>)

    @Query("DELETE FROM rates WHERE base_code = :code")
    suspend fun deleteByBaseCode(code: String)

    @Query("DELETE FROM rates")
    suspend fun deleteAll()

    @Query("SELECT * FROM rates WHERE base_code = :baseCode")
    suspend fun getByBaseCode(baseCode: String): List<RateEntity>

}