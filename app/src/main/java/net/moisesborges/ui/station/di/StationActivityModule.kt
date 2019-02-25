package net.moisesborges.ui.station.di

import net.moisesborges.model.Station
import net.moisesborges.ui.station.mvvm.StationViewModel
import org.koin.dsl.module.module

val stationActivityModule = module {
    factory { (station: Station) -> StationViewModel(station, get()) }
}