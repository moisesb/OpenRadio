package net.moisesborges.ui.main.mvvm

data class MainActivityState(val radioSelection: RadioSelection)

enum class RadioSelection {
    TOP, FAVOURITES
}

fun initialMainActivityState() = MainActivityState(RadioSelection.TOP)