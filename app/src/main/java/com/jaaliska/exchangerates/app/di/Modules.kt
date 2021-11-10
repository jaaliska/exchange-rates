package com.jaaliska.exchangerates.app.di

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

val network = module {
    provideRetrofits()
    provideApis()
}

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
val app = network + repositoryModule + viewModels + serviceModule + dataModule + useCaseModule + dataSources