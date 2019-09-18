package net.moisesborges.ui.navigation

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class TabNavigator {

    private val tabSection: Subject<TabSection> = BehaviorSubject.create()

    fun currentTabSection(): Observable<TabSection> = tabSection

    fun navigateToHome() {
        tabSection.onNext(TabSection.HOME)
    }

    fun navigateToRecentSearches() {
        tabSection.onNext(TabSection.RECENT_SEARCHES)
    }

    fun navigateToMyStations() {
        tabSection.onNext(TabSection.MY_STATIONS)
    }
}

enum class TabSection {
    HOME, MY_STATIONS, RECENT_SEARCHES
}