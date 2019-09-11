package net.moisesborges.ui.home.mvvm

import net.moisesborges.model.Station

data class TopStationsState(
    val stations: List<Station>,
    val loading: Boolean
)

fun initialTopStationsState() = TopStationsState(emptyList(), false)