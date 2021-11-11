package com.jaaliska.exchangerates.data.rates_snapshot.dao.model.snapshot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "rates_snapshot")
data class RoomRatesSnapshot(

    @PrimaryKey
    @ColumnInfo(name = "base_currency_code")
    val baseCurrencyCode: String,

    @ColumnInfo(name = "date")
    val date: Date
)