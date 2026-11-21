package com.example.secondalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val isRepeating = intent?.getBooleanExtra(EXTRA_IS_REPEATING, false) ?: false

        // Queue the next occurrence before opening the ringing screen.
        if (isRepeating && AlarmScheduler.isRepeatActive(context)) {
            AlarmScheduler.scheduleNext(
                context,
                System.currentTimeMillis() + 30L * 60L * 1000L
            )
        }

        val ringIntent = Intent(context, AlarmRingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(ringIntent)
    }

    companion object {
        const val EXTRA_IS_REPEATING = "is_30_minute_repeat"
    }
}
