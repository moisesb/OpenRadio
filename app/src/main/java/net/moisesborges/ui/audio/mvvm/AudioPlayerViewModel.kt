package net.moisesborges.ui.audio.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.Image
import net.moisesborges.model.Station
import net.moisesborges.ui.audio.player.AudioPlayer
import net.moisesborges.ui.navigation.Navigator

class AudioPlayerViewModel(
    private val station: Station,
    private val audioPlayer: AudioPlayer,
    private val navigator: Navigator
) {

    private val disposables = CompositeDisposable()
    private val state: MutableLiveData<AudioPlayerState> = MutableLiveData<AudioPlayerState>().also {
        it.value = initialAudioPlayerState(station)
    }

    val title: LiveData<String> = Transformations.map(state) { state -> state.station.name }
    val image: LiveData<Image> = Transformations.map(state) { state -> state.station.image }
    val isPlaying: LiveData<Boolean> = Transformations.map(state) { state -> state.playbackState.playing }

    init {
        disposables += audioPlayer.playbackState()
            .subscribe { playbackState ->
                state.value = state.get().copy(playbackState = playbackState)
            }
        val stream = station.stream
        if (stream != null) {
            audioPlayer.load(stream.streamUrl)
        }
    }

    fun playPause() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause()
        } else {
            audioPlayer.play()
        }
    }

    fun closeSelected() {
        navigator.navigateBack()
    }
}