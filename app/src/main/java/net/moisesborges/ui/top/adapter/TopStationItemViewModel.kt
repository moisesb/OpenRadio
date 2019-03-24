package net.moisesborges.ui.top.adapter

import androidx.lifecycle.MutableLiveData
import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.utils.RxSchedulers

class TopStationItemViewModel(
    private val station: Station,
    private val navigator: Navigator,
    private val favoriteStationManager: FavoriteStationManager,
    private val rxSchedulers: RxSchedulers
) {

    val title = station.name
    val description = station.countryCode
    val image = station.image
    val isFavorite = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    init {
        favoriteStationManager.favoriteState(station.id)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .subscribe(this::onFavoriteStateChanged)
    }

    fun itemSelected() {
        navigator.navigateToAudioPlayer(station)
    }

    fun favoriteSelected() {
        favoriteStationManager.toggleState(station)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .subscribe(this::onFavoriteStateChanged)
    }

    private fun onFavoriteStateChanged(saved: Boolean) {
        isFavorite.value = saved
    }
}