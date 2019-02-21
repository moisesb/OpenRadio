package net.moisesborges.ui.navigation

import net.moisesborges.model.Station
import net.moisesborges.ui.audio.createAudioPlayerActivityIntent

class Navigator(
    private val activityProvider: ActivityProvider
) {

    fun navigateBack() {
        activityProvider.activity?.finish()
    }

    fun navigateToAudioPlayer(station: Station) {
        val activity = activityProvider.activity
        if (activity != null) {
            val intent = createAudioPlayerActivityIntent(station, activity)
            activity.startActivity(intent)
        }
    }
}