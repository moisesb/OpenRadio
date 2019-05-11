package net.moisesborges.ui.search.di

import net.moisesborges.ui.search.mvvm.SearchViewModel
import org.koin.dsl.module.module

val searchActivityModule = module {
    factory { SearchViewModel(get(), get()) }
}