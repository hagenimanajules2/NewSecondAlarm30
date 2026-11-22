package com.example.secondalarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val isRepeating = intent?.getBooleanExtra(EXTRA_IS_REPEATING, false) ?: false

        // Schedule the next repeat before displaying the current alarm.
        if (isRepeating && AlarmScheduler.isRepeatActive(context)) {
            AlarmScheduler.scheduleNext(
                context,
                System.currentTimeMillis() + 30L * 60L * 1000L
            )
        }

        showAlarmNotification(context)
    }

    private fun showAlarmNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Full-screen alerts for Second Alarm"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setBypassDnd(false)
            }
            manager.createNotificationChannel(channel)
        }

        val ringIntent = Intent(context, AlarmRingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val ringPendingIntent = PendingIntent.getActivity(
            context,
            RING_ACTIVITY_REQUEST_CODE,
            ringIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("SECOND ALARM")
            .setContentText("Alarm is ringing. Tap to open and stop it.")
            .setCategory(Notification.CATEGORY_ALARM)
            .setPriority(Notification.PRIORITY_MAX)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .setContentIntent(ringPendingIntent)
            .setFullScreenIntent(ringPendingIntent, true)
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val EXTRA_IS_REPEATING = "is_30_minute_repeat"
        const val NOTIFICATION_ID = 3001
        const val CHANNEL_ID = "second_alarm_full_screen"
        private const val RING_ACTIVITY_REQUEST_CODE = 3002
    }
}
