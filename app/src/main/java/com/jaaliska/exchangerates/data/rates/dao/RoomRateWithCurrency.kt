package com.jaaliska.exchangerates.data.rates.dao

import androidx.room.Embedded
import androidx.room.Relation
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrency
import com.jaaliska.exchangerates.domain.model.Rate

data class RoomRateWithCurrency(

    @Embedded
    val rate: RoomRate,

    @Relation(parentColumn = "currency_code", entityColumn = "code")
    val currency: RoomCurrency
) {
    fun toDomain() = Rate(rate = rate.rate, currency = currency.toDomain())
}