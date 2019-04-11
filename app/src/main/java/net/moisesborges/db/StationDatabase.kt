package net.moisesborges.db

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters

@Database(entities = [GenreEntity::class, StationEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class StationDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao
}