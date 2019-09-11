package net.moisesborges.ui.home.di

import net.moisesborges.ui.home.mvvm.PaginationDetector
import net.moisesborges.ui.home.mvvm.PaginationLoader
import net.moisesborges.ui.home.mvvm.PaginationManager
import net.moisesborges.ui.home.adapter.TopStationItemViewModelFactory
import net.moisesborges.ui.home.mvvm.HomeViewModel
import org.koin.dsl.module.module

val homeFragmentModule = module {
    single { PaginationManager() }
    single<PaginationLoader> { get<PaginationManager>() }
    single<PaginationDetector> { get<PaginationManager>() }
    single { HomeViewModel(get(), get()) }
    factory { TopStationItemViewModelFactory(get(), get(), get()) }
}