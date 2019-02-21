package net.moisesborges.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import net.moisesborges.ui.navigation.ActivityProvider
import org.koin.android.ext.android.get

abstract class LifecycleActivity : AppCompatActivity(), LifecycleOwner {

    private val activityProvider: ActivityProvider = get()

    override fun onResume() {
        super.onResume()
        activityProvider.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        activityProvider.onPause()
    }
}