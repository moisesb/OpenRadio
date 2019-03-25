package net.moisesborges.db

import androidx.room.RoomDatabase
import androidx.room.Database

@Database(entities = arrayOf(GenreEntity::class, StationEntity::class), version = 1, exportSchema = false)
abstract class StationDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao
}