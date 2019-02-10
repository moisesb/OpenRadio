package net.moisesborges.di

import android.content.Context
import org.koin.dsl.module.module

val appModule = { context: Context ->
    module {
        factory { context.resources }
    }
}