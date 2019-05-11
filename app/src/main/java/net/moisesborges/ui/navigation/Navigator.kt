package net.moisesborges.ui.navigation

import android.app.Activity
import net.moisesborges.model.Station
import net.moisesborges.ui.search.createSearchActivityIntent
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

    fun navigateToSearch() {
        navigateToActivity { currentActivity ->
            val intent = createSearchActivityIntent(currentActivity)
            currentActivity.startActivity(intent)
        }
    }

    private fun navigateToActivity(createIntentAndStart: (activity: Activity) -> Unit) {
        val currentActivity = activityProvider.activity
        if (currentActivity != null) {
            createIntentAndStart(currentActivity)
        }
    }
}