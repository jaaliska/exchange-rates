package com.jaaliska.exchangerates.app.di

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
val app =
    network + repositoryModule + viewModels + serviceModule + dataModule + useCaseModule + dataSources