/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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