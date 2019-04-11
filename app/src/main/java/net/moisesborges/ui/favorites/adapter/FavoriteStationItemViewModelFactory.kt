package net.moisesborges.ui.favorites.adapter

import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator

class FavoriteStationItemViewModelFactory(private val navigator: Navigator) {

    fun create(station: Station) =
            FavoriteStationItemViewModel(station, navigator)
}