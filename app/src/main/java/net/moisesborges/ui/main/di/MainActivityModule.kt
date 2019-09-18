package net.moisesborges.ui.main.di

import net.moisesborges.ui.main.mvvm.MainViewModel
import org.koin.dsl.module.module

val mainActivityModule = module {
    factory { MainViewModel(get(), get()) }
}