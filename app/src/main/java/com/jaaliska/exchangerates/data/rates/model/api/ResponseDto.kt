package com.jaaliska.exchangerates.data.rates.model.api

import com.google.gson.annotations.SerializedName
import java.util.*

data class ResponseDto(
    val response: RatesDetailsDto
) {
    data class RatesDetailsDto(
        val date: Date,
        @SerializedName("base")
        val baseCurrency: String,
        val rates: Map<String, Double>
    )
}