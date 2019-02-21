package net.moisesborges.ui.top.adapter

import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator
import timber.log.Timber

class TopStationItemViewModel(
    private val station: Station,
    private val navigator: Navigator
) {

    val title = station.name
    val description = station.countryCode
    val image = station.image

    fun itemSelected() {
        navigator.navigateToAudioPlayer(station)
    }

    fun favoriteSelected() {
        Timber.d("favorte $station")
    }
}