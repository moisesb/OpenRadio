package net.moisesborges.ui.home.adapter

import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.utils.RxSchedulers

class TopStationItemViewModelFactory(
    private val navigator: Navigator,
    private val favoriteStationManager: FavoriteStationManager,
    private val rxSchedulers: RxSchedulers
) {

    fun create(station: Station) =
        TopStationItemViewModel(
            station,
            navigator,
            favoriteStationManager,
            rxSchedulers
        )
}