package net.moisesborges.ui.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.AudioPlayerNotificationManager
import net.moisesborges.audioplayer.PlaybackState
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.ImageUrl
import net.moisesborges.model.Station

class AudioPlayerViewModel(
    private val audioPlayer: AudioPlayer,
    private val audioPlayerNotificationManager: AudioPlayerNotificationManager
) {

    private val disposables = CompositeDisposable()
    private val state: MutableLiveData<AudioPlayerState> = MutableLiveData<AudioPlayerState>().also {
        it.value = initialAudioPlayerState()
    }

    val title: LiveData<String> = Transformations.map(state) { state -> state.currentStation.name }
    val imageUrl: LiveData<ImageUrl> = Transformations.map(state) { state -> state.currentStation.imageUrl }
    val isPlaying: LiveData<Boolean> = Transformations.map(state) { state -> state.playbackState.playing }
    val isEmpty: LiveData<Boolean> = Transformations.map(state) { state ->
        state.currentStation == Station.EMPTY_STATION
    }

    init {
        disposables += audioPlayer.playbackState()
            .subscribe { playbackState ->
                state.value = state.get().copy(playbackState = playbackState)
                updateNotification(playbackState)
            }
    }

    fun prepareNextStation(station: Station) {
        state.value = state.get().copy(nextStation = station)
    }

    fun playPause() {
        val nextStation = state.get().nextStation
        if (nextStation != null) {
            playNextStation(nextStation)
        } else {
            togglePlayPause()
        }
    }

    fun clear() {
        state.value = state.get().copy(nextStation = null)
    }

    private fun playNextStation(nextStation: Station) {
        val stream = nextStation.streamUrl
            audioPlayer.load(stream.url)
            audioPlayer.play()
            state.value = state.get().copy(currentStation = nextStation, nextStation = null)
    }

    private fun togglePlayPause() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause()
        } else {
            audioPlayer.play()
        }
    }

    private fun updateNotification(playbackState: PlaybackState) {
        val currentStation = state.get().currentStation
        if (currentStation == Station.EMPTY_STATION) {
            return
        }
        val playerState = if (playbackState.playing) AudioPlayerNotificationManager.PlayerState.PLAYING
        else AudioPlayerNotificationManager.PlayerState.STOPED
        audioPlayerNotificationManager.createOrUpdateNotification(currentStation, playerState)
    }
}