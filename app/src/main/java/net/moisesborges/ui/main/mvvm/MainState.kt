package net.moisesborges.ui.main.mvvm

data class MainActivityState(val pageSelection: PageSelection)

enum class PageSelection {
    HOME, MY_STATIONS, RECENT_SEARCHES
}

fun initialMainActivityState() = MainActivityState(PageSelection.HOME)