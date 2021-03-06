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

package net.moisesborges.ui.home.adapter

import androidx.lifecycle.MutableLiveData
import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.utils.RxSchedulers

class StationItemViewModel(
    private val station: Station,
    private val navigator: Navigator,
    private val favoriteStationManager: FavoriteStationManager,
    private val rxSchedulers: RxSchedulers
) {

    val title = station.name
    val description = station.countryCode
    val image = station.imageUrl
    val isFavorite = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    init {
        favoriteStationManager.favoriteState(station.id)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .subscribe(this::onFavoriteStateChanged)
    }

    fun itemSelected() {
        navigator.navigateToAudioPlayer(station)
    }

    fun favoriteSelected() {
        favoriteStationManager.toggleState(station)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .subscribe(this::onFavoriteStateChanged)
    }

    private fun onFavoriteStateChanged(saved: Boolean) {
        isFavorite.value = saved
    }
}