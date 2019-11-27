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

package net.moisesborges.ui.stations.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.api.OpenRadioApi
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.Station
import net.moisesborges.ui.base.mutableLiveDateWith
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.ui.station.mvvm.StationsState
import net.moisesborges.ui.station.mvvm.initialStationsState
import net.moisesborges.utils.RxSchedulers
import timber.log.Timber

class StationsViewModel(
    private val filter: Filter,
    private val navigator: Navigator,
    private val openRadioApi: OpenRadioApi,
    rxSchedulers: RxSchedulers
) {

    private val disposables = CompositeDisposable()

    private val state: MutableLiveData<StationsState> = mutableLiveDateWith(initialStationsState())

    val stations: LiveData<List<Station>> = Transformations.map(state) { it.stations }
    val isLoading: LiveData<Boolean> = Transformations.map(state) { it.isLoading }
    val title: String = when (filter) {
        is Filter.Genre -> filter.genreName
    }

    init {
        state.postValue(state.get().copy(isLoading = true))
        disposables += fetchStations()
            .subscribeOn(rxSchedulers.io())
            .subscribe({ stations ->
                state.postValue(state.get().copy(isLoading = false, stations = stations))
            }, { error ->
                // TODO: handle error
                Timber.e(error)
            })
    }

    private fun fetchStations(): Single<List<Station>> {
        return when (filter) {
            is Filter.Genre -> openRadioApi.getStationsByGenre(filter.genreName)
        }
    }

    fun backButtonSelected() {
        navigator.navigateBack()
    }

    sealed class Filter {

        data class Genre(val genreName: String) : Filter()
    }
}