package com.jaaliska.exchangerates.data.dao

import androidx.room.*
import com.jaaliska.exchangerates.data.model.database.BaseCurrencyEntity

@Dao
interface BaseCurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: BaseCurrencyEntity)

    @Query("DELETE FROM base_currency")
    suspend fun deleteAll()

    @Query("SELECT * FROM base_currency WHERE code = :code")
    suspend fun getByCode(code: String): BaseCurrencyEntity?
}