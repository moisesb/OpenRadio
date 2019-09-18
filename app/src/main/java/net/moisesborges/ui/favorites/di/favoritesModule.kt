package net.moisesborges.ui.favorites.di

import net.moisesborges.ui.favorites.adapter.FavoriteStationItemViewModelFactory
import net.moisesborges.ui.favorites.mvvm.FavoritesViewModel
import org.koin.dsl.module.module

val favoritesModule = module {
    single { FavoritesViewModel(get(), get(), get()) }
    factory { FavoriteStationItemViewModelFactory(get()) }
}