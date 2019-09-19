/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.moisesborges

import android.app.Application
import net.moisesborges.api.di.apiModule
import net.moisesborges.db.di.databaseModule
import net.moisesborges.di.appModule
import net.moisesborges.features.di.featuresModule
import net.moisesborges.features.location.LocationProvider
import net.moisesborges.ui.audioplayer.di.audioPlayerModule
import net.moisesborges.ui.favorites.di.favoritesModule
import net.moisesborges.ui.station.di.stationActivityModule
import net.moisesborges.ui.main.di.mainActivityModule
import net.moisesborges.ui.search.di.searchActivityModule
import net.moisesborges.ui.home.di.homeFragmentModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import timber.log.Timber.DebugTree
import timber.log.Timber

class App : Application() {

    private val locationProvider: LocationProvider by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules())
        setupTimber()
        locationProvider.init()
    }

    private fun appModules(): List<Module> {
        return listOf(
            appModule(applicationContext),
            apiModule,
            mainActivityModule,
            homeFragmentModule,
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
}