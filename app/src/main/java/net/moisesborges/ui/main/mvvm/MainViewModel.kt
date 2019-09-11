package net.moisesborges.ui.main.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import net.moisesborges.extensions.get
import net.moisesborges.ui.navigation.Navigator

class MainViewModel(private val navigator: Navigator) : ViewModel() {

    private val state: MutableLiveData<MainActivityState> = MutableLiveData<MainActivityState>().also {
        it.value = initialMainActivityState()
    }

    val pageSelection: LiveData<PageSelection> = Transformations.map(state) { it.pageSelection }

    fun homeSelected() {
        setPageSelection(PageSelection.HOME)
    }

    fun myStationsSelected() {
        setPageSelection(PageSelection.MY_STATIONS)
    }

    fun recentSearchesSelected() {
        setPageSelection(PageSelection.RECENT_SEARCHES)
    }

    private fun setPageSelection(pageSelection: PageSelection) {
        state.value = state.get().copy(pageSelection = pageSelection)
    }

    fun searchSelected() {
        navigator.navigateToSearch()
    }
}
