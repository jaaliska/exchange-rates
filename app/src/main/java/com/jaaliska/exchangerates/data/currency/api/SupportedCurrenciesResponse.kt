package com.jaaliska.exchangerates.data.currency.api

import com.google.gson.annotations.SerializedName
import com.jaaliska.exchangerates.domain.model.Currency

data class SupportedCurrenciesResponse(
    val response: ResponseResponseDto
) {
    data class ResponseResponseDto(
        val fiats: Map<String, CurrencyDto>
    )

    data class CurrencyDto(
        @SerializedName("currency_name")
        val name: String,
        @SerializedName("currency_code")
        val code: String
    ) {
        fun toDomain() = Currency(name = name, code = code)
    }
}