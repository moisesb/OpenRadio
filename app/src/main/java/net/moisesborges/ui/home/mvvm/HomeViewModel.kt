package net.moisesborges.ui.home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.api.OpenRadioApi
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.Station
import net.moisesborges.utils.RxSchedulers
import timber.log.Timber

class HomeViewModel(
    private val openRadioApi: OpenRadioApi,
    private val paginationLoader: PaginationLoader,
    private val rxSchedulers: RxSchedulers
) {

    private val disposables = CompositeDisposable()
    private val state: MutableLiveData<TopStationsState> = MutableLiveData<TopStationsState>().also {
        it.value = initialTopStationsState()
    }

    val stations: LiveData<List<Station>> = Transformations.map(state) { it.stations }
    val isLoading: LiveData<Boolean> = Transformations.map(state) { it.loading }

    init {
        // TODO: remove pagination from top stations
        loadTopRadios(0)
    }

    fun clear() {
        disposables.clear()
    }

    private fun loadTopRadios(pageNumber: Int) {
        state.value = state.get().copy(loading = true)
        disposables += openRadioApi.getStations()
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .subscribe(this::onStationsLoaded, this::onErrorHappened)
    }

    private fun onErrorHappened(error: Throwable) {
        Timber.e(error)
        state.value = state.get().copy(stations = emptyList(), loading = false)
        paginationLoader.onLoadFailed()
    }

    private fun onStationsLoaded(stations: List<Station>) {
        val topStationsState = state.get()
        state.value = topStationsState.copy(stations = topStationsState.stations + stations, loading = false)
        paginationLoader.onPageLoaded()
    }
}