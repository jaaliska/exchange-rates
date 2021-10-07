package com.jaaliska.exchangerates.data.model.latestRates

import com.google.gson.annotations.SerializedName
import java.util.*

data class RatesDetailsDto(
    val date: Date,
    @SerializedName("base")
    val baseCurrency: String,
    val rates: Map<String, Double>
)