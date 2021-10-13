package com.jaaliska.exchangerates.app.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jaaliska.exchangerates.data.dao.BaseCurrencyDao
import com.jaaliska.exchangerates.data.dao.RateDao
import com.jaaliska.exchangerates.data.model.database.BaseCurrencyEntity
import com.jaaliska.exchangerates.data.model.database.RateEntity

@Database(entities = [BaseCurrencyEntity::class, RateEntity::class], version = 1)
@TypeConverters(RoomDateConverter::class)
abstract class ExchangeRatesDatabase : RoomDatabase() {

    abstract fun baseCurrencyDao(): BaseCurrencyDao
    abstract fun rateEntityDao(): RateDao

}