package net.moisesborges

import android.app.Application
import net.moisesborges.di.appModule
import net.moisesborges.ui.main.di.mainActivityModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, mainActivityModule))
    }
}