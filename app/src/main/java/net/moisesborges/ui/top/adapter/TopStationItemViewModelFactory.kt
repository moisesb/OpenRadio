package net.moisesborges.ui.top.adapter

import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator

class TopStationItemViewModelFactory(
    private val navigator: Navigator
) {

    fun create(station: Station) =
            TopStationItemViewModel(station, navigator)
}