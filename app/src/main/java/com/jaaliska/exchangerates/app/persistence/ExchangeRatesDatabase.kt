package com.jaaliska.exchangerates.app.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jaaliska.exchangerates.data.dao.CurrencyDao
import com.jaaliska.exchangerates.data.dao.RateDao
import com.jaaliska.exchangerates.data.dao.ExchangeRateBaseCurrencyDao
import com.jaaliska.exchangerates.data.model.database.RoomCurrency
import com.jaaliska.exchangerates.data.model.database.RoomExchangeRates
import com.jaaliska.exchangerates.data.model.database.RoomExchangeRateBaseCurrency

@Database(
    entities = [RoomCurrency::class, RoomExchangeRates::class, RoomExchangeRateBaseCurrency::class],
    version = 1
)
@TypeConverters(RoomDateConverter::class)
abstract class ExchangeRatesDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
    abstract fun exchangesRatesDao(): RateDao
    abstract fun exchangeRateBaseCurrencyDao(): ExchangeRateBaseCurrencyDao

}