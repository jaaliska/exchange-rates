package com.jaaliska.exchangerates.data.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(tableName = "exchange_rate_base_currency")
data class RoomExchangeRateBaseCurrency(
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,
    @ColumnInfo(name = "update_date")
    val updateDate: Date
)
