package net.moisesborges.audioplayer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.net.toUri
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlinx.android.parcel.Parcelize
import net.moisesborges.R
import net.moisesborges.extensions.plusAssign
import net.moisesborges.ui.station.createStationActivityIntent
import net.moisesborges.utils.BitmapFactory
import net.moisesborges.utils.RxSchedulers
import org.koin.android.ext.android.inject
import java.lang.IllegalArgumentException

private const val APPLICATION_NAME = "Open Radio"
private const val MEDIA_ROOT = "MY_MEDIA_ROOT"
private const val MEDIA_SESSION_TAG = "net.moisesborges.mediasession.tag"
private const val NOTIFICATION_CHANNEL_ID = "audioControlsNotification"
private const val NOTIFICATION_ID = 1
private const val EVENT_PARAM_ARG = "AudioPlayerService.event"

class AudioPlayerService : MediaBrowserServiceCompat() {

    private val playerDelegate: ExoPlayer by inject()
    private val context: Context by inject()
    private val audioPlayerServiceBinder: AudioPlayerServiceBinder by inject()
    private val bitmapFactory: BitmapFactory by inject()
    private val rxSchedulers: RxSchedulers by inject()

    private val disposables = CompositeDisposable()

    private var playbackState: PlaybackState = PlaybackState(false, null)
    set(value) {
        field = value
        audioPlayerServiceBinder.updatePlaybackState(value)
    }
    private var currentStationImage: Bitmap? = null

    private var httpDataSourceFactory: DataSource.Factory =
        DefaultHttpDataSourceFactory(Util.getUserAgent(context, APPLICATION_NAME))

    private val playerListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            this@AudioPlayerService.playbackState = this@AudioPlayerService.playbackState.copy(isPlaying = playWhenReady)
        }
    }

    private val hlsMediaSourceFactory = HlsMediaSource.Factory(httpDataSourceFactory)

    private val mediaSession: MediaSessionCompat by lazy {
        MediaSessionCompat(context, MEDIA_SESSION_TAG).apply {
            isActive = true
        }
    }

    private val mediaSessionConnector: MediaSessionConnector by lazy {
        MediaSessionConnector(mediaSession)
    }

    private val mediaDescriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val stationInfo = playbackState.stationInfo ?: return null
            val stationActivityIntent = createStationActivityIntent(stationInfo.stationId, context)
            return PendingIntent.getActivity(context, 0, stationActivityIntent, 0)
        }

        override fun getCurrentContentText(player: Player): String? {
            return null
        }

        override fun getCurrentContentTitle(player: Player): String {
            return playbackState.stationInfo?.name ?: ""
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return currentStationImage
        }
    }

    private lateinit var playerNotificationManager: PlayerNotificationManager

    init {
        playerDelegate.addListener(playerListener)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        if (playerDelegate.playWhenReady) {
            val mediaMetadata = MediaMetadataCompat.Builder()
                .build()
            result.sendResult(mutableListOf(MediaBrowserCompat.MediaItem(mediaMetadata.description, 0)))
        } else {
            mediaSessionConnector.mediaSession.sendSessionEvent("NetworkFailed", null)
            result.sendResult(null)
        }
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        val extras = Bundle()
        return BrowserRoot(MEDIA_ROOT, extras)
    }

    override fun onCreate() {
        super.onCreate()
        mediaSessionConnector.setPlayer(playerDelegate, null)
        sessionToken = mediaSessionConnector.mediaSession.sessionToken
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            NOTIFICATION_CHANNEL_ID,
            R.string.audio_channel_name,
            NOTIFICATION_ID,
            mediaDescriptionAdapter
        ).apply {
            setUseNavigationActions(false)
            setRewindIncrementMs(0)
            setFastForwardIncrementMs(0)
        }
        playerNotificationManager.setNotificationListener(object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationCancelled(notificationId: Int) {
                stopSelf()
            }

            override fun onNotificationStarted(notificationId: Int, notification: Notification?) {
                startForeground(notificationId, notification)
            }
        })
        playerNotificationManager.setPlayer(playerDelegate)
        playerNotificationManager.setMediaSessionToken(mediaSession.sessionToken)
    }

    override fun onDestroy() {
        playbackState = playbackState.copy(isPlaying = false, stationInfo = null)
        currentStationImage = null
        mediaSession.release()
        mediaSessionConnector.setPlayer(null, null)
        playerDelegate.release()
        disposables.clear()

        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (val event: Parcelable? = intent?.extras?.getParcelable(EVENT_PARAM_ARG)) {
            is Event.LoadStation -> {
                load(event)
            }
            is Event.Play -> {
                play()
            }
            is Event.Stop -> {
                stop()
            }
            else -> throw IllegalStateException("Intent must include the AudioPlayerService.Event")
        }
        return Service.START_STICKY
    }

    private fun load(loadEvent: Event.LoadStation) {
        loadStationImage(loadEvent.imageUrl)
        playbackState = PlaybackState(false, StationInfo(loadEvent.id, loadEvent.name, loadEvent.imageUrl))
        playerDelegate.prepare(hlsMediaSourceFactory.createMediaSource(loadEvent.streamUri.toUri()))
        play()
    }

    private fun loadStationImage(imageUrl: String?) {
        currentStationImage = null
        disposables += bitmapFactory.decode(imageUrl)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .subscribe { bitmap ->
                currentStationImage = bitmap
            }
    }

    private fun play() {
        playerDelegate.playWhenReady = true
    }

    private fun stop() {
        playerDelegate.playWhenReady = false
    }

    class AudioPlayerServiceBinder {

        private val playbackStateSubject: Subject<PlaybackState> = BehaviorSubject.create()

        fun playbackState(): Observable<PlaybackState> {
            return playbackStateSubject.distinctUntilChanged()
        }

        fun updatePlaybackState(state: PlaybackState) {
            playbackStateSubject.onNext(state)
        }
    }

    sealed class Event {

        @Parcelize
        data class LoadStation(
            val id: Int,
            val name: String,
            val imageUrl: String?,
            val streamUri: String
        ) : Event(), Parcelable

        @Parcelize
        object Play : Event(), Parcelable

        @Parcelize
        object Stop : Event(), Parcelable
    }

    class Launcher(private val context: Context) {

        fun launch(event: Event) {
            val audioPlayerServiceIntent = Intent(context, AudioPlayerService::class.java).apply {
                if (event is Parcelable) {
                    putExtra(EVENT_PARAM_ARG, event)
                } else {
                    throw IllegalArgumentException("$event must implement Parcelable interface")
                }
            }
            Util.startForegroundService(context, audioPlayerServiceIntent)
        }
    }

    data class PlaybackState(val isPlaying: Boolean, val stationInfo: StationInfo?)

    data class StationInfo(val stationId: Int, val name: String, val imageUrl: String?)
}