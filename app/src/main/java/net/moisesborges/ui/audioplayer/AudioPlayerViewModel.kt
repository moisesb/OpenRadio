package net.moisesborges.ui.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.ImageUrl
import net.moisesborges.model.Station

class AudioPlayerViewModel(private val audioPlayer: AudioPlayer) {

    private val disposables = CompositeDisposable()
    private val state: MutableLiveData<AudioPlayerState> = MutableLiveData<AudioPlayerState>().also {
        it.value = initialAudioPlayerState()
    }

    val title: LiveData<String> = Transformations.map(state) { state -> state.currentStation.name }
    val imageUrl: LiveData<ImageUrl> = Transformations.map(state) { state -> state.currentStation.imageUrl }
    val isPlaying: LiveData<Boolean> = Transformations.map(state) { state -> state.playbackState.isPlaying }
    val isEmpty: LiveData<Boolean> = Transformations.map(state) { state ->
        state.currentStation == Station.EMPTY_STATION
    }

    init {
        disposables += audioPlayer.playbackState()
            .subscribe { playbackState ->
                var newState = state.get().copy(playbackState = playbackState)
                if (!playbackState.isLoaded) {
                    newState = newState.copy(nextStation = newState.currentStation, currentStation = Station.EMPTY_STATION)
                }

                state.value = newState
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
        audioPlayer.load(nextStation)
        state.value = state.get().copy(currentStation = nextStation, nextStation = null)
    }

    private fun togglePlayPause() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause()
        } else {
            audioPlayer.play()
        }
    }
}