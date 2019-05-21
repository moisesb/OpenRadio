package net.moisesborges.audioplayer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import net.moisesborges.R
import net.moisesborges.model.Station

private const val CHANNEL_ID = "audioControlsNotification"

class AudioPlayerNotificationManager(private val context: Context) {

    fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createOrUpdateNotification(currentPlayingStation: Station) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(currentPlayingStation.name)
                .build()

        val notifcationManager = NotificationManagerCompat.from(context)
        notifcationManager.notify(2, notification)
    }
}