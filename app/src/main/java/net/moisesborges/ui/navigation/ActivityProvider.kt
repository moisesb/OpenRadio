package net.moisesborges.ui.navigation

import android.app.Activity

class ActivityProvider {

    var activity: Activity? = null
        private set

    fun onResume(activity: Activity) {
        this.activity = activity
    }

    fun onPause() {
        this.activity = null
    }
}