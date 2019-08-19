package net.moisesborges.audioplayer

import io.reactivex.Observable
import net.moisesborges.audioplayer.AudioPlayerService.Launcher
import net.moisesborges.audioplayer.AudioPlayerService.Event
import net.moisesborges.audioplayer.AudioPlayerService.AudioPlayerServiceBinder
import net.moisesborges.model.Station

class AudioPlayer(
    private val audioPlayerServiceBinder: AudioPlayerServiceBinder,
    private val audioServiceLauncher: Launcher
) {

    private var state: AudioPlayerState = AudioPlayerState(false)

    init {
        audioPlayerServiceBinder.playbackState()
            .subscribe { playbackState ->
                state = state.copy(isPlaying = playbackState.isPlaying)
            }
    }

    fun load(station: Station) {
        audioPlayerServiceBinder.sendEvent(Event.LoadStation(station.id, station.name, station.imageUrl.url, station.streamUrl.url))
        audioServiceLauncher.launch()
    }

    fun play() {
        audioPlayerServiceBinder.sendEvent(Event.Play)
        audioServiceLauncher.launch()
    }

    fun pause() {
        audioPlayerServiceBinder.sendEvent(Event.Stop)
    }

    fun isPlaying() = state.isPlaying

    fun playbackState(): Observable<PlaybackState> = audioPlayerServiceBinder.playbackState()
        .map { PlaybackState(it.isPlaying) }
}

private data class AudioPlayerState(val isPlaying: Boolean)