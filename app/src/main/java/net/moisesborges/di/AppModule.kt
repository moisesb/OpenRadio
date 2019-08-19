package net.moisesborges.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.AudioPlayerService
import net.moisesborges.ui.navigation.ActivityProvider
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.utils.BitmapFactory
import net.moisesborges.utils.RxSchedulers
import org.koin.dsl.module.module

val appModule = { context: Context ->
    module {
        factory { context.resources }
        single { ActivityProvider() }
        single { Navigator(get()) }
        factory<ExoPlayer> { ExoPlayerFactory.newSimpleInstance(get()) }
        single { AudioPlayerService.Launcher(get()) }
        single { AudioPlayer(get(), get()) }
        single { AudioPlayerService.AudioPlayerServiceBinder() }
        single { RxSchedulers() }
        single { BitmapFactory() }
    }
}