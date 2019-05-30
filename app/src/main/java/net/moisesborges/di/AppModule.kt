package net.moisesborges.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.AudioPlayerBroadcastReceiver
import net.moisesborges.audioplayer.AudioPlayerNotificationManager
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
        single<ExoPlayer> { ExoPlayerFactory.newSimpleInstance(get()) }
        single { AudioPlayer(get(), get()) }
        single { RxSchedulers() }
        single { BitmapFactory() }
        single { AudioPlayerNotificationManager(get(), get(), get()) }
        single { AudioPlayerBroadcastReceiver(get()) }
    }
}