package net.moisesborges.features.di

import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.features.location.LocationProvider
import org.koin.dsl.module.module

val featuresModule = module {
    single { FavoriteStationManager(get()) }
    single { LocationProvider(get()) }
}