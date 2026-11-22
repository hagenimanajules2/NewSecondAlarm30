package com.example.secondalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmScheduler {
    const val PREFS = "second_alarm_settings"
    const val KEY_REPEAT_ACTIVE = "repeat_30_active"
    private const val REPEAT_REQUEST_CODE = 2001
    private const val INTERVAL_MILLIS = 30L * 60L * 1000L

    fun isRepeatActive(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_REPEAT_ACTIVE, false)

    fun startRepeating(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_REPEAT_ACTIVE, true).apply()
        scheduleNext(context, System.currentTimeMillis() + INTERVAL_MILLIS)
    }

    fun stopRepeating(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_REPEAT_ACTIVE, false).apply()
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.cancel(repeatingPendingIntent(context))
    }

    fun scheduleNext(context: Context, triggerAtMillis: Long) {
        if (!isRepeatActive(context)) return
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            repeatingPendingIntent(context)
        )
    }

    private fun repeatingPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra(AlarmReceiver.EXTRA_IS_REPEATING, true)
        return PendingIntent.getBroadcast(
            context,
            REPEAT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun restoreAfterReboot(context: Context) {
        if (isRepeatActive(context)) {
            // Restart the 30-minute countdown from the time the phone boots.
            scheduleNext(context, System.currentTimeMillis() + INTERVAL_MILLIS)
        }
    }
}
