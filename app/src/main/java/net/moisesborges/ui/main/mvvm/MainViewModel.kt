package net.moisesborges.ui.main.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.extensions.plusAssign
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.ui.navigation.TabNavigator
import net.moisesborges.ui.navigation.TabSection

class MainViewModel(
    private val navigator: Navigator,
    private val tabNavigator: TabNavigator
) : ViewModel() {

    private val mutableTabSection = MutableLiveData<TabSection>()

    private val disposables = CompositeDisposable()

    val tabSection: LiveData<TabSection> = Transformations.map(mutableTabSection) { it }

    init {
        disposables += tabNavigator.currentTabSection()
            .subscribe { this.mutableTabSection.postValue(it) }
    }

    fun homeSelected() {
        tabNavigator.navigateToHome()
    }

    fun myStationsSelected() {
        tabNavigator.navigateToMyStations()
    }

    fun recentSearchesSelected() {
        tabNavigator.navigateToRecentSearches()
    }

    fun searchSelected() {
        navigator.navigateToSearch()
    }

    fun clear() {
        disposables.clear()
    }
}
