package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.repository.RoomCurrencyRepository
import com.jaaliska.exchangerates.domain.model.Currency
import com.jaaliska.exchangerates.domain.usecases.GetNamedRatesUseCase
import com.jaaliska.exchangerates.domain.usecases.GetRatesUseCase
import com.jaaliska.exchangerates.presentation.model.NamedExchangeRates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class GetNamedRatesUseCaseImpl (
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val getRatesUseCase: GetRatesUseCase
) : GetNamedRatesUseCase {

    override suspend operator fun invoke(baseCurrencyCode: String): Flow<NamedExchangeRates> {
        val favorites = localCurrencyRepository.readFavoriteCurrencies()
        val baseCode = if(baseCurrencyCode == "") {
            favorites.first()
        } else { baseCurrencyCode }
        val codesToLoad = favorites.toMutableList()
        if (!codesToLoad.contains(baseCode)) {
            codesToLoad.add(baseCode)
        }

        val currencies =
            localCurrencyRepository.readSupportedCurrencies(codesToLoad).associateBy { it.code }
        val rates = getRatesUseCase(baseCode, favorites)
        val getCurrency = { code: String -> currencies[code] ?: Currency("", code) }

        return rates.map {
            NamedExchangeRates(
                date = it.date,
                baseCurrency = getCurrency(it.baseCurrencyCode),
                rates = it.rates.map {
                    Pair(
                        getCurrency(it.currencyCode),
                        it.exchangeRate
                    )
                }
            )
        }
    }
}