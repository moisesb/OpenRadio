package net.moisesborges.ui.search.mvvm

import net.moisesborges.model.Station

data class SearchState(
    val query: String,
    val isLoading: Boolean,
    val result: List<Station>,
    val error: SearchError?
)

fun initialSearchState() = SearchState("", false, emptyList(), null)

sealed class SearchError {
    object NotFound : SearchError()
    data class RequestError(val message: String) : SearchError()
}