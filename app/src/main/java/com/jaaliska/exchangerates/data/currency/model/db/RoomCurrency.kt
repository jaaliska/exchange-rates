package com.jaaliska.exchangerates.data.currency.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class RoomCurrency(
    @PrimaryKey
    val code: String,
    val name: String,
    val isFavorite: Boolean
)
