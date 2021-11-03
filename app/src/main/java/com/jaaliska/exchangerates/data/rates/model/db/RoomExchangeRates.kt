package com.jaaliska.exchangerates.data.rates.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "rates", primaryKeys = ["base_code", "code"])
data class RoomExchangeRates (
    @ColumnInfo(name = "base_code")
    val baseCurrencyCode: String,
    @ColumnInfo(name = "code")
    val currencyCode: String,
    @ColumnInfo(name = "rate")
    val rate: Double
)