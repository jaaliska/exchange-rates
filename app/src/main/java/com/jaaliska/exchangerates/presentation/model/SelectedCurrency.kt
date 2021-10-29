package com.jaaliska.exchangerates.presentation.model

data class SelectedCurrency(
    val name: String,
    val code: String,
    var isSelected: Boolean
)
