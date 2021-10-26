package com.jaaliska.exchangerates.data.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "rates")
data class RoomExchangeRates (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "base_code")
    val baseCurrencyCode: String,
    @ColumnInfo(name = "code")
    val currencyCode: String,
    @ColumnInfo(name = "rate")
    val rate: Double
)