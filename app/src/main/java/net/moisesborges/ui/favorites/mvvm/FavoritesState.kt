package net.moisesborges.ui.favorites.mvvm

import net.moisesborges.model.Station

data class FavoritesState(val isLoading: Boolean, val favorites: List<Station>)

fun initialFavoritesState() = FavoritesState(false, emptyList())