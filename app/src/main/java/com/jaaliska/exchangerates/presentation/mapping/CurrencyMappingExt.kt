package com.jaaliska.exchangerates.presentation.mapping

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.BaseCurrencyChoiceDialogViewModel.SelectableItem

fun Currency.toSelectableItem(isSelected: Boolean) =
    SelectableItem(
        subtitle = name,
        title = code,
        isSelected = isSelected
    )

fun SelectableItem.toCurrency() = Currency(
    name = subtitle,
    code = title
)