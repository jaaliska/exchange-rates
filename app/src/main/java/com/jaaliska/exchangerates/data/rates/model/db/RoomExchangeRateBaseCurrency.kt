package com.jaaliska.exchangerates.data.rates.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "exchange_rate_base_currency")
data class RoomExchangeRateBaseCurrency(
    @PrimaryKey
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,
    @ColumnInfo(name = "update_date")
    val updateDate: Date
)
