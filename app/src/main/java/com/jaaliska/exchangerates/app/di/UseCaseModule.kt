package com.jaaliska.exchangerates.app.di

import com.jaaliska.exchangerates.data.usecase.SetAnchorCurrencyUseCaseImpl
import com.jaaliska.exchangerates.data.usecase.UpdateCurrencyFavoriteStateUseCaseImpl
import com.jaaliska.exchangerates.domain.usecase.SetAnchorCurrencyUseCase
import com.jaaliska.exchangerates.domain.usecase.UpdateCurrencyFavoriteStateUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal val useCaseModule = module {

    factory<SetAnchorCurrencyUseCase> {
        SetAnchorCurrencyUseCaseImpl(
            anchorCurrencyRepository = get()
        )
    }

    factory<UpdateCurrencyFavoriteStateUseCase> {
        UpdateCurrencyFavoriteStateUseCaseImpl(
            dao = get()
        )
    }
}