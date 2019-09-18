package net.moisesborges.ui.favorites.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import net.moisesborges.extensions.get
import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.TabNavigator
import net.moisesborges.utils.RxSchedulers

class FavoritesViewModel(
    private val favoriteStationManager: FavoriteStationManager,
    private val rxSchedulers: RxSchedulers,
    private val tabNavigator: TabNavigator
) {
    private var disposable: Disposable = Disposables.empty()

    private val state = MutableLiveData<FavouritesState>().also {
        it.value = initialFavouritesState()
    }

    val isLoading: LiveData<Boolean> = Transformations.map(state) { it.isLoading }
    val isEmpty: LiveData<Boolean> = Transformations.map(state) { !it.isLoading && it.favorites.isEmpty() }
    val favorites: LiveData<List<Station>> = Transformations.map(state) { it.favorites }

    init {
        state.value = state.get().copy(isLoading = true)
        disposable = favoriteStationManager.favoriteStations()
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .subscribe(this::handleStationsLoaded, this::handleError)
    }

    fun exploreSelected() {
        tabNavigator.navigateToHome()
    }

    private fun handleStationsLoaded(stations: List<Station>) {
        state.value = state.get().copy(isLoading = false, favorites = stations)
    }

    private fun handleError(error: Throwable) {
        // TODO: Handle error
        state.value = state.get().copy(isLoading = false)
    }
}