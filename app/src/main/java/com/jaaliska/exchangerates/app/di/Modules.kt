package com.jaaliska.exchangerates.app.di

import org.koin.dsl.module

val network = module {
    provideRetrofits()
    provideApis()
}

val app = network + repositoryModule + viewModels + serviceModule