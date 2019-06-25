package net.moisesborges.features.search

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import net.moisesborges.api.OpenRadioApi
import java.util.concurrent.TimeUnit

class SearchEngine(
    private val api: OpenRadioApi
) {

    private val querySubject = PublishSubject.create<String>()

    fun setSearchQuery(searchQuery: String) {
        querySubject.onNext(searchQuery)
    }

    fun searchResult(): Observable<SearchResult> {
        return querySubject.debounce(300, TimeUnit.MILLISECONDS)
            .flatMap { searchQuery ->
                api.searchStations(searchQuery, "")
                    .map { stations -> SearchResult(false, stations) }
                    .toObservable()
            }
    }
}