package net.moisesborges.ui.top.di

import net.moisesborges.ui.top.mvvm.PaginationDetector
import net.moisesborges.ui.top.mvvm.PaginationLoader
import net.moisesborges.ui.top.mvvm.PaginationManager
import net.moisesborges.ui.top.adapter.TopStationItemViewModelFactory
import net.moisesborges.ui.top.mvvm.TopStationsViewModel
import org.koin.dsl.module.module

val topFragmentsModule = module {
    single { PaginationManager() }
    single<PaginationLoader> { get<PaginationManager>() }
    single<PaginationDetector> { get<PaginationManager>() }
    single { TopStationsViewModel(get(), get()) }
    factory { TopStationItemViewModelFactory(get(), get(), get()) }
}