package net.moisesborges

import android.app.Application
import net.moisesborges.api.di.apiModule
import net.moisesborges.audioplayer.AudioPlayerBroadcastReceiver
import net.moisesborges.audioplayer.AudioPlayerNotificationManager
import net.moisesborges.db.di.databaseModule
import net.moisesborges.di.appModule
import net.moisesborges.features.di.featuresModule
import net.moisesborges.features.location.LocationProvider
import net.moisesborges.model.Station
import net.moisesborges.ui.audioplayer.di.audioPlayerModule
import net.moisesborges.ui.favorites.di.favoritesModule
import net.moisesborges.ui.station.di.stationActivityModule
import net.moisesborges.ui.main.di.mainActivityModule
import net.moisesborges.ui.search.di.searchActivityModule
import net.moisesborges.ui.top.di.topFragmentsModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import timber.log.Timber.DebugTree
import timber.log.Timber

class App : Application() {

    private val locationProvider: LocationProvider by inject()
    private val notificationManager: AudioPlayerNotificationManager by inject()
    private val audioPlayerBroadcastReceiver: AudioPlayerBroadcastReceiver by inject()
    private val audioPlayerNotificationManager: AudioPlayerNotificationManager by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules())
        setupTimber()
        locationProvider.init()
        notificationManager.init()
        registerReceivers()
    }

    private fun appModules(): List<Module> {
        return listOf(
            appModule(applicationContext),
            apiModule,
            mainActivityModule,
            topFragmentsModule,
            favoritesModule,
            audioPlayerModule,
            stationActivityModule,
            databaseModule,
            featuresModule,
            searchActivityModule
        )
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return "${super.createStackElementTag(element)}:${element.lineNumber}"
                }
            })
        }
    }

    private fun registerReceivers() {
        audioPlayerBroadcastReceiver.register(this)
    }
}