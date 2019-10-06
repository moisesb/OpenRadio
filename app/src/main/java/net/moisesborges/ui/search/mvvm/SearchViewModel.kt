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

package net.moisesborges.ui.search.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import net.moisesborges.extensions.get
import net.moisesborges.features.search.SearchEngine
import net.moisesborges.utils.RxSchedulers

class SearchViewModel(
    private val searchEngine: SearchEngine,
    private val rxSchedulers: RxSchedulers,
    private val stringResolver: SearchStringResolver
) {

    private var disposable: Disposable = Disposables.empty()
    private val state = MutableLiveData<SearchState>().also {
        it.value = initialSearchState()
    }

    val result: LiveData<List<SearchItem>> = Transformations.map(state, this::searchItemsMapper)

    fun start() {
        disposable = searchEngine.searchResult()
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .doOnError { error ->
                state.value = state.get().copy(isLoading = false,
                    result = emptyList(),
                    error = SearchError.RequestError(error.message ?: ""))
            }
            .retry()
            .subscribe { searchResult ->
                state.value = state.get().copy(isLoading = searchResult.isPartial, result = searchResult.stations)
            }
    }

    fun search(searchQuery: String) {
        if (searchQuery.isNotBlank()) {
            state.value = state.get().copy(isLoading = true, hasSearchStarted = true, query = searchQuery, error = null)
            searchEngine.setSearchQuery(searchQuery)
        } else {
            state.value = state.get().copy(isLoading = false, hasSearchStarted = false, query = searchQuery, error = null, result = emptyList())
        }
    }

    fun clear() {
        disposable.dispose()
        state.value = state.get().copy(hasSearchStarted = false)
    }

    private fun searchItemsMapper(state: SearchState): List<SearchItem> {
        if (state.error != null && state.result.isEmpty()) {
            val message = stringResolver.somethingWentWrongMessage()
            return listOf(SearchItem.ErrorMessage(message))
        }
        val stationResultItems = state.result.map(SearchItem::Station)
        val progressResultItems: List<SearchItem> =
            if (state.isLoading) listOf(SearchItem.ProgressIndicator) else emptyList()
        val searchResult = stationResultItems + progressResultItems
        return if (searchResult.isNotEmpty() || !state.hasSearchStarted) {
            searchResult
        } else {
            val message = stringResolver.emptyResultsForQuery(state.query)
            listOf(SearchItem.EmptyResultsMessage(message))
        }
    }
}