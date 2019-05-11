package net.moisesborges.ui.search.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import net.moisesborges.extensions.get
import net.moisesborges.features.search.SearchEngine
import net.moisesborges.utils.RxSchedulers

class SearchViewModel(private val searchEngine: SearchEngine, private val rxSchedulers: RxSchedulers) {

    private var disposable: Disposable = Disposables.empty()
    private val state = MutableLiveData<SearchState>().also {
        it.value = initialSearchState()
    }

    val result: LiveData<List<SearchItem>> = Transformations.map(state) {
        val stationResultItems = it.result.map(SearchItem::Station)
        val progressResultItems: List<SearchItem> = if (it.isLoading) listOf(SearchItem.ProgressIndicator) else emptyList()
        stationResultItems + progressResultItems
    }

    fun start() {
        disposable = searchEngine.searchResult()
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .retry()
            .subscribe({ searchResult ->
                state.value = state.get().copy(isLoading = searchResult.isPartial, result = searchResult.stations)
            }, { error ->
                state.value = state.get().copy(isLoading = false,
                    result = emptyList(),
                    error = SearchError.RequestError(error.message ?: ""))
            })
    }

    fun search(searchQuery: String) {
        state.value = state.get().copy(isLoading = true)
        searchEngine.setSearchQuery(searchQuery)
    }

    fun clear() {
        disposable.dispose()
    }
}