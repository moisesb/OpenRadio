package net.moisesborges.di

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.AudioPlayerBroadcastReceiver
import net.moisesborges.audioplayer.AudioPlayerNotificationManager
import net.moisesborges.ui.navigation.ActivityProvider
import net.moisesborges.ui.navigation.Navigator
import net.moisesborges.utils.BitmapFactory
import net.moisesborges.utils.RxSchedulers
import org.koin.dsl.module.module

private const val MEDIA_SESSION_TAG = "net.moisesborges.mediasession.tag"

val appModule = { context: Context ->
    module {
        factory { context.resources }
        single { ActivityProvider() }
        single { Navigator(get()) }
        single<ExoPlayer> { ExoPlayerFactory.newSimpleInstance(get()) }
        single { MediaSessionConnector(get()) }
        single {
            MediaSessionCompat(context, MEDIA_SESSION_TAG).apply {
                setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            }
        }
        single { AudioPlayer() }
        single { RxSchedulers() }
        single { BitmapFactory() }
        single { AudioPlayerNotificationManager(get(), get(), get(), get()) }
        single { AudioPlayerBroadcastReceiver(get()) }
    }
}