package com.jaaliska.exchangerates.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jaaliska.exchangerates.domain.datasource.RatesDataSource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@DelicateCoroutinesApi
class AlertReceiver : BroadcastReceiver(), KoinComponent {

    private val ratesDataSource by inject<RatesDataSource>()

    override fun onReceive(context: Context, intent: Intent) {
        GlobalScope.launch {
            ratesDataSource.refresh()
            val notificationHelper = NotificationHelper(context)
            notificationHelper.manager?.notify(1, notificationHelper.buildChannelNotification())
        }
    }
}