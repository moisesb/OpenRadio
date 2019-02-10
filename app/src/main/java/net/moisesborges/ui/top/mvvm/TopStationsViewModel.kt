package net.moisesborges.ui.top.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.moisesborges.api.DirbleApi
import net.moisesborges.extensions.get
import net.moisesborges.model.Station
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TopStationsViewModel(
    private val dirbleApi: DirbleApi
) {

    private val state: MutableLiveData<TopStationsState> = MutableLiveData<TopStationsState>().also {
        it.value = initialTopStationsState()
    }

    val stations: LiveData<List<Station>> = Transformations.map(state) { it.stations }

    val isLoading: LiveData<Boolean> = Transformations.map(state) { it.loading }

    fun loadTopRadios() {
        state.value = state.get().copy(loading = true)
        val disposable = dirbleApi.getPopularStations()
            .delay(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onStationsLoaded, this::onErrorHappened)
    }

    private fun onErrorHappened(error: Throwable) {
        Timber.e(error)
        state.value = state.get().copy(stations = emptyList(), loading = false)
    }

    private fun onStationsLoaded(stations: List<Station>) {
        state.value = state.get().copy(stations = stations, loading = false)
    }
}