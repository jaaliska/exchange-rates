package com.jaaliska.exchangerates.data.usecase

import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates.api.RetrofitRatesRepository
import com.jaaliska.exchangerates.data.rates.dao.RoomRatesRepository
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.repository.AnchorCurrencyRepository
import com.jaaliska.exchangerates.domain.usecases.GetRatesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

@ExperimentalCoroutinesApi
class GetNamedRatesUseCaseImpl(
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val remoteRatesRepository: RetrofitRatesRepository,
    private val localRatesRepository: RoomRatesRepository,
    private val anchorCurrencyRepository: AnchorCurrencyRepository
) : GetRatesUseCase {

    override operator fun invoke(): Flow<ExchangeRates?> {
        return anchorCurrencyRepository.getAnchorCurrencyCode()
            .flatMapLatest { anchorCurrencyCode ->
                localCurrencyRepository.readFavoriteCurrencies().map {
                    val favorites = it.toMutableList()
                    val anchorCurrency =
                        favorites.find { it.code == anchorCurrencyCode } ?: favorites.firstOrNull()
                    anchorCurrency ?: return@map null
                    favorites.remove(anchorCurrency)
                    val rates = remoteRatesRepository.getRates(
                        anchorCurrency,
                        favorites.apply { remove(anchorCurrency) })
                    ExchangeRates(date = Date(), baseCurrency = anchorCurrency, rates = rates)
                }
            }
    }
}