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

package net.moisesborges.ui.favorites.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import net.moisesborges.extensions.get
import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.TabNavigator
import net.moisesborges.utils.RxSchedulers

class FavoritesViewModel(
    private val favoriteStationManager: FavoriteStationManager,
    private val rxSchedulers: RxSchedulers,
    private val tabNavigator: TabNavigator
) {
    private var disposable: Disposable = Disposables.empty()

    private val state = MutableLiveData<FavouritesState>().also {
        it.value = initialFavouritesState()
    }

    val isLoading: LiveData<Boolean> = Transformations.map(state) { it.isLoading }
    val isEmpty: LiveData<Boolean> = Transformations.map(state) { !it.isLoading && it.favorites.isEmpty() }
    val favorites: LiveData<List<Station>> = Transformations.map(state) { it.favorites }

    init {
        state.value = state.get().copy(isLoading = true)
        disposable = favoriteStationManager.favoriteStations()
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .subscribe(this::handleStationsLoaded, this::handleError)
    }

    fun exploreSelected() {
        tabNavigator.navigateToHome()
    }

    private fun handleStationsLoaded(stations: List<Station>) {
        state.value = state.get().copy(isLoading = false, favorites = stations)
    }

    private fun handleError(error: Throwable) {
        // TODO: Handle error
        state.value = state.get().copy(isLoading = false)
    }
}