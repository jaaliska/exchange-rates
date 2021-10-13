package com.jaaliska.exchangerates.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jaaliska.exchangerates.domain.model.ExchangeRates
import com.jaaliska.exchangerates.domain.model.ResultWrapper
import com.jaaliska.exchangerates.domain.repository.CurrencyRepository
import com.jaaliska.exchangerates.domain.repository.PreferencesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent

class AlertReceiver: BroadcastReceiver(), KoinComponent {

    private val currencyRepository: CurrencyRepository by KoinJavaComponent.inject(
        CurrencyRepository::class.java
    )
    private val prefsRepository: PreferencesRepository by KoinJavaComponent.inject(
        PreferencesRepository::class.java
    )
    private val alarmService: AlarmService by KoinJavaComponent.inject(AlarmService::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        GlobalScope.launch {
            val result = currencyRepository.getExchangeRates(
                prefsRepository.getBaseCurrencyCode(),
                true
            )
            if (result is ResultWrapper.Success<ExchangeRates>) {
                alarmService.exchangeRatesCacheUpdated.emit(result.value)
                val notificationHelper = NotificationHelper(context)
                notificationHelper.manager?.notify(1, notificationHelper.buildChannelNotification())
            }
            alarmService.startAlarm()
        }
    }
}