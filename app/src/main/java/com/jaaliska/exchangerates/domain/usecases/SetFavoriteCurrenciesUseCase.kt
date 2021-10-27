package com.jaaliska.exchangerates.domain.usecases


interface SetFavoriteCurrenciesUseCase {
    suspend operator fun invoke(codes: List<String>)
}