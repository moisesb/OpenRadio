package net.moisesborges.ui.favorites.adapter

import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator

class FavoriteStationItemViewModel(
    private val station: Station,
    private val navigator: Navigator
) {

    val image = station.imageUrl

    fun itemSelected() {
        navigator.navigateToAudioPlayer(station)
    }
}