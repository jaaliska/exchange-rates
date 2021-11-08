package com.jaaliska.exchangerates.app.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jaaliska.exchangerates.data.currency.dao.CurrencyDao
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrency
import com.jaaliska.exchangerates.data.rates.dao.RateDao
import com.jaaliska.exchangerates.data.rates.dao.RoomRate

@Database(
    entities = [RoomCurrency::class, RoomRate::class],
    version = 1
)
@TypeConverters(RoomDateConverter::class)
abstract class ExchangeRatesDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
    abstract fun ratesDao(): RateDao
}