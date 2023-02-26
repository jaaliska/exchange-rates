package com.jaaliska.exchangerates.presentation.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.util.*

class AlarmService(
    private val context: Context
) {

    fun startAlarm() {
        cancelAlarm()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis() + DAY_IN_MILLIS
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        alarmManager!!.setExact(AlarmManager.RTC, calendar.timeInMillis, createPendingIntent())
        Timber.tag("AlarmService").d("startAlarm ${calendar.time}")
    }

    private fun cancelAlarm() {
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        alarmManager!!.cancel(createPendingIntent())
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, AlertReceiver::class.java)
        return PendingIntent.getBroadcast(context, 1, intent, FLAG_IMMUTABLE)
    }

    companion object {
        private const val DAY_IN_MILLIS = 24 * 60 * 60 * 1000
    }
}