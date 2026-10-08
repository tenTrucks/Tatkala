package com.example.tatkala.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tatkala.R

class ReminderWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val title = inputData.getString(KEY_TITLE) ?: "Tatakala reminder"
        val message = inputData.getString(KEY_MESSAGE) ?: "Time to check your schedule."
        ensureChannel()

        val launchIntent = applicationContext.packageManager
            .getLaunchIntentForPackage(applicationContext.packageName)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            inputData.getInt(KEY_NOTIFICATION_ID, DEFAULT_NOTIFICATION_ID),
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri())
            .setVibrate(longArrayOf(0, 140, 90, 140))
            .build()

        runCatching {
            NotificationManagerCompat.from(applicationContext).notify(
                inputData.getInt(KEY_NOTIFICATION_ID, DEFAULT_NOTIFICATION_ID),
                notification
            )
        }
        return Result.success()
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Tatakala reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Task and habit reminder alerts"
            enableVibration(true)
            setSound(
                soundUri(),
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
        }
        val manager = applicationContext.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun soundUri(): Uri = Uri.parse(
        "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${applicationContext.packageName}/${R.raw.tatakala_clean_ping}"
    )

    companion object {
        const val CHANNEL_ID = "tatakala_reminders_v2"
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
        const val KEY_NOTIFICATION_ID = "notification_id"
        private const val DEFAULT_NOTIFICATION_ID = 1104
    }
}
