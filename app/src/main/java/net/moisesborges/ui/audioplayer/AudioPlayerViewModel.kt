package net.moisesborges.ui.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.PlaybackState
import net.moisesborges.audioplayer.initialPlaybackState
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.ImageUrl
import net.moisesborges.model.Station

class AudioPlayerViewModel(private val audioPlayer: AudioPlayer) {

    private val disposables = CompositeDisposable()
    private val state: MutableLiveData<PlaybackState> = MutableLiveData<PlaybackState>().also {
        it.value = initialPlaybackState()
    }

    val title: LiveData<String> = Transformations.map(state) { it.station.name }
    val imageUrl: LiveData<ImageUrl> = Transformations.map(state) { it.station.imageUrl }
    val isPlaying: LiveData<Boolean> = Transformations.map(state) { it.isPlaying }
    val isEmpty: LiveData<Boolean> = Transformations.map(state) { it.station == Station.EMPTY_STATION }

    init {
        disposables += audioPlayer.playbackState()
            .subscribe { playbackState ->
                state.value = playbackState
            }
    }

    fun playPause() {
        val station = state.get().station
        if (station == Station.EMPTY_STATION) {
            return
        }

        if (audioPlayer.isPlaying()) {
            audioPlayer.pause()
        } else {
            audioPlayer.play()
        }
    }

    fun clear() {
        disposables.clear()
    }
}