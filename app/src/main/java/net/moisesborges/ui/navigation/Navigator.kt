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

package net.moisesborges.ui.navigation

import android.app.Activity
import android.app.ActivityOptions
import android.os.Build
import android.transition.Fade
import net.moisesborges.model.Station
import net.moisesborges.ui.search.createSearchActivityIntent
import net.moisesborges.ui.settings.createSettingsActivityIntent
import net.moisesborges.ui.station.createStationActivityIntent

class Navigator(
    private val activityProvider: ActivityProvider
) {

    fun navigateBack() {
        activityProvider.activity?.finish()
    }

    fun navigateToAudioPlayer(station: Station) {
        navigateToActivity { currentActivity ->
            val intent = createStationActivityIntent(station, currentActivity)
            currentActivity.startActivity(intent)
        }
    }

    fun navigateToAudioPlayer(stationId: Int) {
        navigateToActivity { currentActivity ->
            val intent = createStationActivityIntent(stationId, currentActivity)
            currentActivity.startActivity(intent)
        }
    }

    fun navigateToSearch() {
        navigateToActivity { currentActivity ->
            val intent = createSearchActivityIntent(currentActivity)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                with(currentActivity.window) {
                    exitTransition = Fade()
                }
                currentActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(currentActivity).toBundle())
            } else {
                currentActivity.startActivity(intent)
            }
        }
    }

    private fun navigateToActivity(createIntentAndStart: (activity: Activity) -> Unit) {
        val currentActivity = activityProvider.activity
        if (currentActivity != null) {
            createIntentAndStart(currentActivity)
        }
    }

    fun navigateToSettings() {
        navigateToActivity { currentActivity ->
            currentActivity.startActivity(createSettingsActivityIntent(currentActivity))
        }
    }
}