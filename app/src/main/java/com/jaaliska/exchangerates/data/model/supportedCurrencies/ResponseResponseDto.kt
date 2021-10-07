package com.jaaliska.exchangerates.data.model.supportedCurrencies

import com.jaaliska.exchangerates.domain.model.CurrencyDetails

data class ResponseResponseDto(
    val fiats: Map<String, CurrencyDetails>
)
