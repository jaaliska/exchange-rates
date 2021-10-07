package com.jaaliska.exchangerates.domain.model

import com.google.gson.annotations.SerializedName

data class CurrencyDetails(
    @SerializedName("currency_name")
    val currencyName: String,
    @SerializedName("currency_code")
    val currencyCode: String
)
