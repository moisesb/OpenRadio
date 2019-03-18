package net.moisesborges.features.di

import net.moisesborges.features.favorite.FavoriteStationManager
import org.koin.dsl.module.module

val featuresModule = module {
    single { FavoriteStationManager(get()) }
}