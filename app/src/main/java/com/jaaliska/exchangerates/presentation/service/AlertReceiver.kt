package com.jaaliska.exchangerates.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jaaliska.exchangerates.domain.repository.RatesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent

class AlertReceiver : BroadcastReceiver(), KoinComponent {

    private val ratesRepository: RatesRepository by KoinJavaComponent.inject(
        RatesRepository::class.java
    )

    override fun onReceive(context: Context, intent: Intent) {
        GlobalScope.launch {
            ratesRepository.refresh()
            val notificationHelper = NotificationHelper(context)
            notificationHelper.manager?.notify(1, notificationHelper.buildChannelNotification())
        }
    }
}