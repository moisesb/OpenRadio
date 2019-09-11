package net.moisesborges.ui.station.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.api.OpenRadioApi
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.ImageUrl
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator
import timber.log.Timber

class StationViewModel(
    private val audioPlayer: AudioPlayer,
    private val openRadioApi: OpenRadioApi,
    private val navigator: Navigator
) {

    private val disposables = CompositeDisposable()
    private val state: MutableLiveData<StationState> = MutableLiveData<StationState>().also {
        it.value = initialStationState()
    }

    val title: LiveData<String> = Transformations.map(state) { it.station.name }
    val imageUrl: LiveData<ImageUrl> = Transformations.map(state) { it.station.imageUrl }
    val isPlaying: LiveData<Boolean> = Transformations.map(state) {
        val isPlayingCurrentStation = it.playbackState.station.id == it.station.id
        it.playbackState.isPlaying && isPlayingCurrentStation
    }
    val isLoading: LiveData<Boolean> = Transformations.map(state) { it.isLoadingStation }

    init {
        disposables += audioPlayer.playbackState()
            .subscribe { playbackState ->
                state.value = state.get().copy(playbackState = playbackState)
            }
    }

    fun prepareStation(station: Station) {
        state.value = state.get().copy(station = station)
    }

    fun prepareStation(stationId: Int) {
        disposables += openRadioApi.getStation(stationId)
            .subscribe({ station ->
                state.postValue(state.get().copy(station = station))
            }, {
                state.postValue(state.get().copy(error = Error.StationNotLoadedError))
            })
    }

    fun playPause() {
        val stationState = state.get()
        if (stationState.station == Station.EMPTY_STATION) {
            Timber.d("Must load station before play")
            return
        }

        if (stationState.playbackState.station.id != stationState.station.id) {
            playStation(stationState.station)
        } else {
            togglePlayPause()
        }
    }

    fun closeSelected() {
        navigator.navigateBack()
    }

    fun clear() {
        disposables.clear()
    }

    private fun playStation(nextStation: Station) {
        audioPlayer.load(nextStation)
    }

    private fun togglePlayPause() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause()
        } else {
            audioPlayer.play()
        }
    }
}