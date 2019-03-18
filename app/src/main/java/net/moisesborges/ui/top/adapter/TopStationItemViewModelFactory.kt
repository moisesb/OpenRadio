package net.moisesborges.ui.top.adapter

import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator

class TopStationItemViewModelFactory(
    private val navigator: Navigator,
    private val favoriteStationManager: FavoriteStationManager
) {

    fun create(station: Station) =
            TopStationItemViewModel(station, navigator, favoriteStationManager)
}