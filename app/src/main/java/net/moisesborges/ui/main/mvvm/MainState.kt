package net.moisesborges.ui.main.mvvm

data class MainActivityState(val pageSelection: PageSelection)

enum class PageSelection {
    TOP_RADIOS, FAVOURITES_RADIOS, SETTINGS
}

fun initialMainActivityState() = MainActivityState(PageSelection.TOP_RADIOS)