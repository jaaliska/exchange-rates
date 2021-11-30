package com.jaaliska.exchangerates.presentation.common.mapping

import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.presentation.common.list.checkable_item.CheckableItem

fun Currency.toSelectableItem(isSelected: Boolean) =
    CheckableItem(
        subtitle = name,
        title = code,
        isSelected = isSelected
    )

fun CheckableItem.toCurrency() = Currency(
    name = subtitle,
    code = title
)