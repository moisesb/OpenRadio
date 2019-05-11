package net.moisesborges.ui.favorites.mvvm

import net.moisesborges.model.Station

data class FavouritesState(val isLoading: Boolean, val favorites: List<Station>)

fun initialFavouritesState() = FavouritesState(false, emptyList())