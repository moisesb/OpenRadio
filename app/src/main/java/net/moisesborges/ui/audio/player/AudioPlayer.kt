package net.moisesborges.ui.audio.player

import android.content.Context
import androidx.core.net.toUri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

private const val APPLICATION_NAME = "Open Radio"

class AudioPlayer(
    private val playerDelegate: ExoPlayer,
    context: Context
) {

    private var dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
        context,
        Util.getUserAgent(context, APPLICATION_NAME)
    )

    private val playerListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            playbackStateSubject.onNext(PlaybackState(playWhenReady))
        }
    }

    private val mediaSourceFactory = ExtractorMediaSource.Factory(dataSourceFactory)
    private val playbackStateSubject: Subject<PlaybackState> = BehaviorSubject.create()

    init {
        playerDelegate.addListener(playerListener)
    }

    fun load(audioUri: String) {
        playerDelegate.prepare(mediaSourceFactory.createMediaSource(audioUri.toUri()))
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