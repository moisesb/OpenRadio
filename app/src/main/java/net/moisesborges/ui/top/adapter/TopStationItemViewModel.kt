package net.moisesborges.ui.top.adapter

import net.moisesborges.model.Station
import timber.log.Timber

class TopStationItemViewModel(
    private val station: Station
) {

    val title = station.name
    val description = station.countryCode
    val image = station.image

    fun itemSelected() {
        Timber.d("item $station selected")
    }

    fun favoriteSelected() {
        Timber.d("favorte $station")
    }
}