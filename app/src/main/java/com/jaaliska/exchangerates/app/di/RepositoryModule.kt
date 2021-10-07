package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.repository.CurrencyRepositoryImpl
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import org.koin.dsl.module

internal val repositoryModule = module {
    single<CurrencyRepository> { CurrencyRepositoryImpl(api = get()) }
}