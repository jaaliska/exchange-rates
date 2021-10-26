package com.jaaliska.exchangerates.data.model.api.supportedCurrencies

data class BodyResponseDto(
    val response: ResponseResponseDto
) {
    data class ResponseResponseDto(
        val fiats: Map<String, CurrencyDto>
    )
}