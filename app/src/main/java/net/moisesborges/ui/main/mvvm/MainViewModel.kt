package net.moisesborges.ui.main.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import net.moisesborges.extensions.get

class MainViewModel : ViewModel() {

    private val state: MutableLiveData<MainActivityState> = MutableLiveData<MainActivityState>().also {
        it.value = initialMainActivityState()
    }

    val pageSelection: LiveData<PageSelection> = Transformations.map(state) { it.pageSelection }

    fun topRadiosSelected() {
        setPageSelection(PageSelection.TOP_RADIOS)
    }

    fun favoritesRadiosSelected() {
        setPageSelection(PageSelection.FAVOURITES_RADIOS)
    }

    fun settingsSelected() {
        setPageSelection(PageSelection.SETTINGS)
    }

    private fun setPageSelection(pageSelection: PageSelection) {
        state.value = state.get().copy(pageSelection = pageSelection)
    }
}
