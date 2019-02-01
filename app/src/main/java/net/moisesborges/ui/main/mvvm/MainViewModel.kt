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

    val radioSelection: LiveData<RadioSelection> = Transformations.map(state) { it.radioSelection }

    fun topSelected() {
        state.value = state.get().copy(radioSelection = RadioSelection.TOP)
    }

    fun favoritesSelected() {
        state.value = state.get().copy(radioSelection = RadioSelection.FAVOURITES)
    }
}