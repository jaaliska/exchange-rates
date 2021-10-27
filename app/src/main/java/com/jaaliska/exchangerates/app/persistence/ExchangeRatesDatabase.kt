package com.jaaliska.exchangerates.app.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jaaliska.exchangerates.data.currency.dao.CurrencyDao
import com.jaaliska.exchangerates.data.rates.dao.RateDao
import com.jaaliska.exchangerates.data.rates.dao.ExchangeRateBaseCurrencyDao
import com.jaaliska.exchangerates.data.currency.model.db.RoomCurrency
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRates
import com.jaaliska.exchangerates.data.rates.model.db.RoomExchangeRateBaseCurrency

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