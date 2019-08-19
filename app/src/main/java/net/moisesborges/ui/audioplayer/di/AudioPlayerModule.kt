package net.moisesborges.ui.audioplayer.di

import net.moisesborges.ui.audioplayer.AudioPlayerViewModel
import org.koin.dsl.module.module

val audioPlayerModule = module {
    single { AudioPlayerViewModel(get()) }
}