package net.moisesborges.ui.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.Image
import net.moisesborges.model.Station

class AudioPlayerViewModel(
    private val audioPlayer: AudioPlayer
) {

    private val disposables = CompositeDisposable()
    private val state: MutableLiveData<AudioPlayerState> = MutableLiveData<AudioPlayerState>().also {
        it.value = initialAudioPlayerState()
    }

    val title: LiveData<String> = Transformations.map(state) { state -> state.currentStation.name }
    val image: LiveData<Image> = Transformations.map(state) { state -> state.currentStation.image }
    val isPlaying: LiveData<Boolean> = Transformations.map(state) { state -> state.playbackState.playing }

    init {
        disposables += audioPlayer.playbackState()
            .subscribe { playbackState ->
                state.value = state.get().copy(playbackState = playbackState)
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
        val stream = nextStation.stream
        if (stream != null) {
            audioPlayer.load(stream.streamUrl)
            audioPlayer.play()
            state.value = state.get().copy(nextStation = null)
        } else {
            // TODO: handle error when stream is not present
        }
    }

    private fun togglePlayPause() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause()
        } else {
            audioPlayer.play()
        }
    }
}