package net.moisesborges

import android.app.Application
import net.moisesborges.api.di.apiModule
import net.moisesborges.di.appModule
import net.moisesborges.ui.main.di.mainActivityModule
import net.moisesborges.ui.top.di.topFragmentsModule
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import timber.log.Timber.DebugTree
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules())

        setupTimber()
    }

    private fun appModules(): List<Module> {
        return listOf(
            appModule(applicationContext),
            apiModule,
            mainActivityModule,
            topFragmentsModule
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