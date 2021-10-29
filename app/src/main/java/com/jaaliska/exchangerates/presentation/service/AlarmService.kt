package com.jaaliska.exchangerates.presentation.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
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
        Log.d("AlarmService", "startAlarm ${calendar.time}")
    }

    private fun cancelAlarm() {
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        alarmManager!!.cancel(createPendingIntent())
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, AlertReceiver::class.java)
        return PendingIntent.getBroadcast(context, 1, intent, 0)
    }

    companion object {
        private const val DAY_IN_MILLIS = 24 * 60 * 60 * 1000
    }
}