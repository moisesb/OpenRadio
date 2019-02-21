package net.moisesborges.ui.audio.di

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import net.moisesborges.model.Station
import net.moisesborges.ui.audio.mvvm.AudioPlayerViewModel
import net.moisesborges.ui.audio.player.AudioPlayer
import org.koin.dsl.module.module

val audioPlayerActivityModule = module {
    single<ExoPlayer> { ExoPlayerFactory.newSimpleInstance(get()) }
    single { AudioPlayer(get(), get()) }
    factory { (station: Station) -> AudioPlayerViewModel(station, get(), get()) }
}