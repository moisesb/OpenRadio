package net.moisesborges.ui.search.mvvm

sealed class SearchItem {

    data class Station(val station: net.moisesborges.model.Station) : SearchItem()
    object ProgressIndicator : SearchItem()
}