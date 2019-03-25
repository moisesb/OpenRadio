package net.moisesborges.db.di

import androidx.room.Room
import net.moisesborges.db.StationDatabase
import net.moisesborges.db.StationsRepository
import org.koin.dsl.module.module

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), StationDatabase::class.java, "station-database")
            .build()
    }

    single { get<StationDatabase>().stationDao() }

    single { StationsRepository(get()) }
}