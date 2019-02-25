package net.moisesborges.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.ui.navigation.ActivityProvider
import net.moisesborges.ui.navigation.Navigator
import org.koin.dsl.module.module

val appModule = { context: Context ->
    module {
        factory { context.resources }
        single { ActivityProvider() }
        single { Navigator(get()) }
        single<ExoPlayer> { ExoPlayerFactory.newSimpleInstance(get()) }
        single { AudioPlayer(get(), get()) }
    }
}