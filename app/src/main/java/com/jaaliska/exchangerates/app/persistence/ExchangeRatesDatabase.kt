package com.jaaliska.exchangerates.app.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jaaliska.exchangerates.data.currency.dao.CurrencyDao
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrency
import com.jaaliska.exchangerates.data.rates_snapshot.dao.RatesSnapshotDao
import com.jaaliska.exchangerates.data.rates_snapshot.dao.model.rate.RoomRate
import com.jaaliska.exchangerates.data.rates_snapshot.dao.model.snapshot.RoomRatesSnapshot

@Database(
    entities = [RoomCurrency::class, RoomRate::class, RoomRatesSnapshot::class],
    version = 1
)
@TypeConverters(RoomDateConverter::class)
abstract class ExchangeRatesDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
    abstract fun ratesSnapshotDao(): RatesSnapshotDao
}