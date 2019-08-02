package net.moisesborges.audioplayer

import android.content.Context
import android.media.MediaDescription
import android.media.browse.MediaBrowser
import android.media.session.MediaSession
import android.os.Bundle
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import org.koin.android.ext.android.inject
import timber.log.Timber

private const val APPLICATION_NAME = "Open Radio"
private const val MEDIA_ROOT = "MY_MEDIA_ROOT"

class AudioPlayer: MediaBrowserServiceCompat() {

    private val playerDelegate: ExoPlayer by inject()
    private val context: Context by inject()
    private val mediaSessionConnector: MediaSessionConnector by inject()

    private var httpDataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory(Util.getUserAgent(context, APPLICATION_NAME))

    private val playerListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            playbackStateSubject.onNext(PlaybackState(playWhenReady))
        }
    }

    private val hlsMediaSourceFactory = HlsMediaSource.Factory(httpDataSourceFactory)
    private val playbackStateSubject: Subject<PlaybackState> = BehaviorSubject.create()

    init {
        playerDelegate.addListener(playerListener)
    }

    override fun onCreate() {
        super.onCreate()
        mediaSessionConnector.setPlayer(playerDelegate, null)
        sessionToken = mediaSessionConnector.mediaSession.sessionToken
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        Timber.d("parent $parentId, result: $result")
        if (playerDelegate.playWhenReady) {
            val mediaMetadata = MediaMetadataCompat.Builder()
                .build()
            result.sendResult(mutableListOf(MediaBrowserCompat.MediaItem(mediaMetadata.description, 0)))
        } else {
            mediaSessionConnector.mediaSession.sendSessionEvent("NetworkFaile", null)
            result.sendResult(null)
        }
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        Timber.d("client $clientPackageName, uuid $clientUid, hints: $rootHints")
        val extras = Bundle()
        return BrowserRoot(MEDIA_ROOT, extras)
    }

    fun load(streamUri: String) {
        playerDelegate.prepare(hlsMediaSourceFactory.createMediaSource(streamUri.toUri()))
    }

    fun play() {
        playerDelegate.playWhenReady = true
    }

    fun pause() {
        playerDelegate.playWhenReady = false
    }

    fun isPlaying() = playerDelegate.playWhenReady

    fun playbackState(): Observable<PlaybackState> =
        playbackStateSubject.distinctUntilChanged()
}