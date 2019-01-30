package net.moisesborges.di

import org.koin.dsl.module.module

val appModule = module {
    factory { HelloWorldProvider() }
}

// This class should be removed
class HelloWorldProvider {
    fun helloWorld() = "Hello World from Koin"
}