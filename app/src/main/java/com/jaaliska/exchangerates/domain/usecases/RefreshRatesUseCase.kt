package com.jaaliska.exchangerates.domain.usecases

interface RefreshRatesUseCase {
    suspend operator fun invoke()
}