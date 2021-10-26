package com.jaaliska.exchangerates.data.model.api.supportedCurrencies

import com.google.gson.annotations.SerializedName

data class CurrencyDto(
    @SerializedName("currency_name")
    val name: String,
    @SerializedName("currency_code")
    val code: String
)
