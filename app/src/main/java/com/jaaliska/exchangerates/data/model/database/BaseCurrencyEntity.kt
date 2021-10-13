package com.jaaliska.exchangerates.data.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "base_currency")
data class BaseCurrencyEntity(
    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "name")
    val name: String
)
