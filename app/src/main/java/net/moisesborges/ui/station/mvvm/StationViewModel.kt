package net.moisesborges.ui.station.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import net.moisesborges.model.ImageUrl
import net.moisesborges.model.Station
import net.moisesborges.ui.navigation.Navigator

class StationViewModel(
    station: Station,
    private val navigator: Navigator
) {

    private val state: MutableLiveData<StationState> = MutableLiveData<StationState>().also {
        it.value = initialStationState(station)
    }

    val title: LiveData<String> = Transformations.map(state) { state -> state.station.name }
    val imageUrl: LiveData<ImageUrl> = Transformations.map(state) { state -> state.station.imageUrl }

    fun closeSelected() {
        navigator.navigateBack()
    }
}