package net.moisesborges.ui.top.adapter

import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator

class TopStationItemViewModel(
    private val station: Station,
    private val navigator: Navigator,
    private val favoriteStationManager: FavoriteStationManager
) {

    val title = station.name
    val description = station.countryCode
    val image = station.image

    fun itemSelected() {
        navigator.navigateToAudioPlayer(station)
    }

    fun favoriteSelected() {
        favoriteStationManager.toggleState(station)
    }
}