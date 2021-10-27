package com.jaaliska.exchangerates.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jaaliska.exchangerates.domain.usecases.RefreshRatesUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent

class AlertReceiver : BroadcastReceiver(), KoinComponent {

    private val refreshRatesUseCase: RefreshRatesUseCase by KoinJavaComponent.inject(
        RefreshRatesUseCase::class.java
    )

    override fun onReceive(context: Context, intent: Intent) {
        GlobalScope.launch {
            refreshRatesUseCase()
            val notificationHelper = NotificationHelper(context)
            notificationHelper.manager?.notify(1, notificationHelper.buildChannelNotification())
        }
    }
}