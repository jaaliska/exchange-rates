package com.jaaliska.exchangerates.data.rates_snapshot.dao.model.snapshot

import androidx.room.Embedded
import androidx.room.Relation
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrency
import com.jaaliska.exchangerates.data.rates_snapshot.dao.model.rate.RoomRate
import com.jaaliska.exchangerates.data.rates_snapshot.dao.model.rate.RoomRateWithCurrency
import com.jaaliska.exchangerates.domain.model.RatesSnapshot

data class RoomRatesSnapshotWithRates(

    @Embedded
    val ratesSnapshot: RoomRatesSnapshot,

    @Relation(parentColumn = "base_currency_code", entityColumn = "code")
    val baseCurrency: RoomCurrency,

    @Relation(
        parentColumn = "base_currency_code",
        entityColumn = "base_currency_code",
        entity = RoomRate::class
    )
    val rates: List<RoomRateWithCurrency>
) {
    constructor(ratesSnapshot: RatesSnapshot) : this(
        ratesSnapshot = RoomRatesSnapshot(
            baseCurrencyCode = ratesSnapshot.baseCurrency.code,
            date = ratesSnapshot.date
        ),
        baseCurrency = RoomCurrency(ratesSnapshot.baseCurrency),
        ratesSnapshot.rates.map {
            RoomRateWithCurrency(
                it,
                baseCurrency = RoomCurrency(ratesSnapshot.baseCurrency)
            )
        }
    )

    fun toDomain(): RatesSnapshot {
        return RatesSnapshot(
            date = ratesSnapshot.date,
            baseCurrency = baseCurrency.toDomain(),
            rates = rates.map(RoomRateWithCurrency::toDomain)
        )
    }
}