package com.jaaliska.exchangerates.data.rates_snapshot

import com.jaaliska.exchangerates.data.anchor_currency.SharedPrefAnchorCurrencyRepository
import com.jaaliska.exchangerates.data.core.registerHooks
import com.jaaliska.exchangerates.data.currency.persistence.CurrencyDao
import com.jaaliska.exchangerates.data.rates_snapshot.api.RetrofitRatesSnapshotRepository
import com.jaaliska.exchangerates.data.rates_snapshot.dao.RatesSnapshotDao
import com.jaaliska.exchangerates.data.rates_snapshot.dao.model.snapshot.RoomRatesSnapshotWithRates
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
    private val ratesSnapshotDao: RatesSnapshotDao,
    private val remoteRatesRepository: RetrofitRatesSnapshotRepository,
    private val currencyDao: CurrencyDao,
    private val anchorCurrencyRepository: SharedPrefAnchorCurrencyRepository,
    private val alarmService: AlarmService,
    coroutineScope: CoroutineScope
) : RatesDataSource {

    init {
        with(coroutineScope) {
            registerHooks(
                currencyDao.readFavorites(),
                action = ::refresh
            )
        }
    }

    override fun observe(): Flow<RatesSnapshot?> {
        return anchorCurrencyRepository.observe().flatMapLatest { anchorCurrencyCode ->
            currencyDao.readFavorites().flatMapLatest { roomCurrencies ->
                val favorites = roomCurrencies.toMutableList()
                val anchorCurrency = favorites.find { it.code == anchorCurrencyCode }
                    ?: favorites.firstOrNull()
                    ?: run {
                        refresh()
                        return@flatMapLatest observe()
                    }

                val localRates = ratesSnapshotDao.read(baseCurrencyCode = anchorCurrency.code)
                    .map { roomRates -> roomRates?.toDomain() }

                if (localRates.first() == null) refresh()

                localRates
            }
        }
    }

    override suspend fun refresh() {
        val anchorCurrencyCode = anchorCurrencyRepository.observe().first()
        val favorites = currencyDao.readFavorites().first()

        val anchorCurrency =
            favorites.find { it.code == anchorCurrencyCode } ?: favorites.firstOrNull() ?: return
        val ratesToLoad =
            favorites.toMutableList().apply { remove(anchorCurrency) }.map { it.toDomain() }
        if (ratesToLoad.isEmpty()) return

        val rates = remoteRatesRepository.getRates(anchorCurrency.toDomain(), ratesToLoad)

        with(ratesSnapshotDao) {
            delete(baseCurrencyCode = anchorCurrency.code)
            insert(RoomRatesSnapshotWithRates(rates))
        }

        alarmService.startAlarm()
    }
}