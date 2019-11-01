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

package net.moisesborges.ui.recentsearches.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.api.OpenRadioApi
import net.moisesborges.extensions.get
import net.moisesborges.extensions.pairWith
import net.moisesborges.extensions.plusAssign
import net.moisesborges.features.recentsearches.RecentSearchReposity
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.utils.RxSchedulers

class RecentSearchesViewModel(
    private val recentSearchRepository: RecentSearchReposity,
    private val openRadioApi: OpenRadioApi,
    private val rxSchedulers: RxSchedulers,
    private val stringResolver: RecentSearchesStringResolver,
    private val navigator: Navigator
) {

    private val disposables = CompositeDisposable()

    private val state: MutableLiveData<RecentSearchState> = MutableLiveData<RecentSearchState>().also {
        it.value = initialRecentSearchState()
    }

    val content: LiveData<List<RecentSearchItem>> = Transformations.map(state, this::mapToRecentSearchItems)

    fun start() {
        val currentState = state.get()
        if (currentState.recentViewedStations.isNullOrEmpty()) {
            state.postValue(currentState.copy(isLoading = true))
        }

        disposables += recentSearchRepository.recentSearches()
            .pairWith(openRadioApi.getGenres())
            .subscribeOn(rxSchedulers.io())
            .subscribe { (recentViewedStations, genres) ->
                state.postValue(state.get().copy(isLoading = false, recentViewedStations = recentViewedStations, genres = genres))
            }
    }

    fun selectSearch() {
        navigator.navigateToSearch()
    }

    private fun mapToRecentSearchItems(state: RecentSearchState): List<RecentSearchItem> {
        if (state.isLoading) {
            return emptyList()
        }
        val content = mutableListOf<RecentSearchItem>()
        val recentViewedStations = state.recentViewedStations
        if (recentViewedStations.isNotEmpty()) {
            content += RecentSearchItem.Header(stringResolver.recentSearchesHeader())
            content += recentViewedStations.map(RecentSearchItem::RecentlyViewedStation)
        }

        val genres = state.genres
        if (genres.isNotEmpty()) {
            content += RecentSearchItem.Header(stringResolver.genresHeader())
            content += genres.map(RecentSearchItem::Genre)
        }
        return content
    }
}