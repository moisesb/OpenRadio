package net.moisesborges.ui.top.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.moisesborges.api.DirbleApi
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.Station
import timber.log.Timber

class TopStationsViewModel(
    private val dirbleApi: DirbleApi,
    private val paginationLoader: PaginationLoader
) {

    private val disposables = CompositeDisposable()
    private val state: MutableLiveData<TopStationsState> = MutableLiveData<TopStationsState>().also {
        it.value = initialTopStationsState()
    }

    val stations: LiveData<List<Station>> = Transformations.map(state) { it.stations }
    val isLoading: LiveData<Boolean> = Transformations.map(state) { it.loading }

    init {
        paginationLoader.loadPage()
            .subscribe { pageNumber ->
                loadTopRadios(pageNumber)
            }
    }

    fun clear() {
        disposables.clear()
    }

    private fun loadTopRadios(pageNumber: Int) {
        state.value = state.get().copy(loading = true)
        disposables += dirbleApi.getPopularStations(pageNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onStationsLoaded, this::onErrorHappened)
    }

    private fun onErrorHappened(error: Throwable) {
        Timber.e(error)
        state.value = state.get().copy(stations = emptyList(), loading = false)
        paginationLoader.onPageLoaded()
    }

    private fun onStationsLoaded(stations: List<Station>) {
        val topStationsState = state.get()
        state.value = topStationsState.copy(stations = topStationsState.stations + stations, loading = false)
        paginationLoader.onPageLoaded()
    }
}