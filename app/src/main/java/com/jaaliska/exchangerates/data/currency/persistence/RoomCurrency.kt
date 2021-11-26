package com.jaaliska.exchangerates.data.currency.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jaaliska.exchangerates.domain.model.Currency

@Entity(tableName = "currency")
data class RoomCurrency(
    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
) {
    constructor(currency: Currency) : this(
        code = currency.code,
        name = currency.name,
        isFavorite = false
    )

    fun toDomain() = Currency(name = name, code = code)
}