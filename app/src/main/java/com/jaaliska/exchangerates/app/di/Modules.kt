package com.jaaliska.exchangerates.app.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

val network = module {
    provideRetrofits()
    provideApis()
}

@FlowPreview
@ExperimentalCoroutinesApi
val app = network + repositoryModule + viewModels + serviceModule + dataModule + useCaseModule