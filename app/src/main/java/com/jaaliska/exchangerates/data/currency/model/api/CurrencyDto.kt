package com.jaaliska.exchangerates.data.currency.model.api

import com.google.gson.annotations.SerializedName

data class CurrencyDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("short_code")
    val code: String
)
