package net.moisesborges.ui.top.di

import net.moisesborges.ui.top.mvvm.TopStationsViewModel
import org.koin.dsl.module.module

val topFragmentsModule = module {
    factory { TopStationsViewModel(get()) }
}