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

    private var state: AudioPlayerState = AudioPlayerState(isPlaying = false)

    init {
        audioPlayerServiceBinder.playbackState()
            .subscribe { playbackState ->
                state = state.copy(isPlaying = playbackState.isPlaying)
            }
    }

    fun load(station: Station) {
        audioServiceLauncher.launch(Event.LoadStation(station.id, station.name, station.imageUrl.url, station.streamUrl.url))
    }

    fun play() {
        audioServiceLauncher.launch(Event.Play)
    }

    fun pause() {
        audioServiceLauncher.launch(Event.Stop)
    }

    fun isPlaying() = state.isPlaying

    fun playbackState(): Observable<PlaybackState> = audioPlayerServiceBinder.playbackState()
        .map { PlaybackState(isPlaying = it.isPlaying, isLoaded = it.stationInfo != null) }
}

private data class AudioPlayerState(val isPlaying: Boolean)