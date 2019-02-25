package net.moisesborges.ui.navigation

import net.moisesborges.model.Station
import net.moisesborges.ui.station.createStationActivityIntent

class Navigator(
    private val activityProvider: ActivityProvider
) {

    fun navigateBack() {
        activityProvider.activity?.finish()
    }

    fun navigateToAudioPlayer(station: Station) {
        val activity = activityProvider.activity
        if (activity != null) {
            val intent = createStationActivityIntent(station, activity)
            activity.startActivity(intent)
        }
    }
}