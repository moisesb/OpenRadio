package net.moisesborges.ui.station.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import net.moisesborges.model.Image
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
    val image: LiveData<Image> = Transformations.map(state) { state -> state.station.image }

    fun closeSelected() {
        navigator.navigateBack()
    }
}