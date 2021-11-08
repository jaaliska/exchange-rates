package com.jaaliska.exchangerates.data.rates.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrency
import com.jaaliska.exchangerates.domain.model.Rate


@Entity(
    tableName = "rates",
    foreignKeys = [ForeignKey(
        entity = RoomCurrency::class,
        parentColumns = ["code"],
        childColumns = ["currency_code"],
        onDelete = CASCADE
    )]
)
data class RoomRate(

    @PrimaryKey
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo(name = "rate")
    val rate: Double
) {
    constructor(rate: Rate) : this(rate = rate.rate, currencyCode = rate.currency.code)
}