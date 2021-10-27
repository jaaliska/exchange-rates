package com.jaaliska.exchangerates.data.currency.model.api

data class BodyResponseDto(
    val response: ResponseResponseDto
) {
    data class ResponseResponseDto(
        val fiats: Map<String, CurrencyDto>
    )
}