package com.jaaliska.exchangerates.data.rates_snapshot

import com.jaaliska.exchangerates.data.anchor_currency.SharedPrefAnchorCurrencyRepository
import com.jaaliska.exchangerates.data.core.registerHooks
import com.jaaliska.exchangerates.data.currency.dao.RoomCurrencyRepository
import com.jaaliska.exchangerates.data.rates_snapshot.api.RetrofitRatesSnapshotRepository
import com.jaaliska.exchangerates.data.rates_snapshot.dao.RoomRatesSnapshotRepository
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import com.jaaliska.exchangerates.domain.model.RatesSnapshot
import com.jaaliska.exchangerates.presentation.service.AlarmService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class MediatorRatesDataSource(
    private val localRatesRepository: RoomRatesSnapshotRepository,
    private val remoteRatesRepository: RetrofitRatesSnapshotRepository,
    private val localCurrencyRepository: RoomCurrencyRepository,
    private val anchorCurrencyRepository: SharedPrefAnchorCurrencyRepository,
    private val alarmService: AlarmService,
    coroutineScope: CoroutineScope
) : RatesDataSource {

    init {
        with(coroutineScope) {
            registerHooks(
                localCurrencyRepository.readFavoriteCurrencies(),
                action = ::refresh
            )
        }
    }

    override fun observe(): Flow<RatesSnapshot?> {
        return anchorCurrencyRepository.observe().flatMapLatest { anchorCurrencyCode ->
            localCurrencyRepository.readFavoriteCurrencies().flatMapLatest {
                val favorites = it.toMutableList()
                val anchorCurrency = favorites.find { it.code == anchorCurrencyCode }
                    ?: favorites.firstOrNull()
                    ?: run {
                        refresh()
                        return@flatMapLatest observe()
                    }

                val localRates = localRatesRepository.read(baseCurrency = anchorCurrency).first()
                if (localRates == null) refresh()

                localRatesRepository.read(baseCurrency = anchorCurrency)
            }
        }
    }

    override suspend fun refresh() {
        val anchorCurrencyCode = anchorCurrencyRepository.observe().first()
        val favorites = localCurrencyRepository.readFavoriteCurrencies().first()

        val anchorCurrency =
            favorites.find { it.code == anchorCurrencyCode } ?: favorites.firstOrNull() ?: return
        val ratesToLoad = favorites.toMutableList().apply { remove(anchorCurrency) }
        if (ratesToLoad.isEmpty()) return

        localRatesRepository.delete(baseCurrency = anchorCurrency)

        val rates = remoteRatesRepository.getRates(anchorCurrency, ratesToLoad)
        localRatesRepository.save(rates)

        alarmService.startAlarm()
    }
}