package net.moisesborges.audioplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import net.moisesborges.R
import net.moisesborges.model.Station
import net.moisesborges.ui.station.createStationActivityIntent
import net.moisesborges.utils.BitmapFactory
import net.moisesborges.utils.RxSchedulers
import timber.log.Timber
import androidx.media.app.NotificationCompat as MediaNotificationCompat

private const val CHANNEL_ID = "audioControlsNotification"
const val NOTIFICATION_ID = 1

// TODO: Prevent user from dismiss the notification if still playing
class AudioPlayerNotificationManager(
    private val context: Context,
    private val mediaSessionCompat: MediaSessionCompat,
    private val bitmapFactory: BitmapFactory,
    private val rxSchedulers: RxSchedulers
) {

    enum class PlayerState {
        PLAYING, STOPPED
    }

    private var disposable: Disposable = Disposables.empty()

    fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.audio_channel_name)
            val descriptionText = context.getString(R.string.audio_channel_description)
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

    fun createOrUpdateNotification(currentPlayingStation: Station, playerState: PlayerState) {

        disposable.dispose()

        disposable = bitmapFactory.decode(currentPlayingStation.imageUrl.url)
            .observeOn(rxSchedulers.io())
            .subscribeOn(rxSchedulers.mainThread())
            .subscribe({ thumbnail ->
                createOrUpdateNotification(currentPlayingStation, thumbnail, playerState)
            }, Timber::e, {
                createOrUpdateNotification(currentPlayingStation, null, playerState)
            })
    }

    fun createOrUpdateNotification(currentPlayingStation: Station, thumbnail: Bitmap?, playerState: PlayerState) : Notification {
        val stationActivityIntent = createStationActivityIntent(currentPlayingStation, context)

        val stationActivityPendingIntent = PendingIntent.getActivity(context, 0, stationActivityIntent, 0)

        val stopPendingIntent = createPlayStopPendingIntent(context)

        val style = MediaNotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0)
            .setMediaSession(mediaSessionCompat.sessionToken)
            .setShowCancelButton(true)
            .setCancelButtonIntent(stopPendingIntent)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(thumbnail)
            .setOnlyAlertOnce(true)
            .setContentTitle(currentPlayingStation.name)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDeleteIntent(stopPendingIntent)
            .setStyle(style)
            .setContentIntent(stationActivityPendingIntent)

        val playStopPendingIntent = createPlayStopPendingIntent(context)

        when (playerState) {
            PlayerState.PLAYING -> {
                builder.addAction(R.drawable.ic_baseline_pause_24px, context.getString(R.string.action_stop), playStopPendingIntent)
            }
            PlayerState.STOPPED -> {
                builder.addAction(R.drawable.ic_baseline_play_arrow_24px, context.getString(R.string.action_play), playStopPendingIntent)
            }
        }

        return builder.build()

//        with(NotificationManagerCompat.from(context)) {
//            notify(NOTIFICATION_ID, builder.build())
//        }
    }
}