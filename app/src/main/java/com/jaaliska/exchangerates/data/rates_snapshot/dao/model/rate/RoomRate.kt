package com.jaaliska.exchangerates.data.rates_snapshot.dao.model.rate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jaaliska.exchangerates.domain.model.RatesSnapshot

@Entity(tableName = "rates")
data class RoomRate(

    @PrimaryKey
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "base_currency_code")
    val baseCurrencyCode: String,

    @ColumnInfo(name = "rate")
    val rate: Double
) {
    constructor(rate: RatesSnapshot.Rate, baseCurrencyCode: String) : this(
        rate = rate.rate,
        baseCurrencyCode = baseCurrencyCode,
        currencyCode = rate.currency.code
    )
}