package com.jaaliska.exchangerates.data.rates_snapshot.dao.model.rate

import androidx.room.Embedded
import androidx.room.Relation
import com.jaaliska.exchangerates.data.currency.persistence.sql.dao.RoomCurrency
import com.jaaliska.exchangerates.domain.model.RatesSnapshot

data class RoomRateWithCurrency(

    @Embedded
    val rate: RoomRate,

    @Relation(parentColumn = "currency_code", entityColumn = "code")
    val currency: RoomCurrency,

    @Relation(parentColumn = "base_currency_code", entityColumn = "code")
    val baseCurrency: RoomCurrency
) {
    constructor(rate: RatesSnapshot.Rate, baseCurrency: RoomCurrency) : this(
        currency = RoomCurrency(rate.currency),
        rate = RoomRate(rate, baseCurrencyCode = baseCurrency.code),
        baseCurrency = baseCurrency
    )

    fun toDomain() = RatesSnapshot.Rate(
        rate = rate.rate,
        currency = currency.toDomain()
    )
}