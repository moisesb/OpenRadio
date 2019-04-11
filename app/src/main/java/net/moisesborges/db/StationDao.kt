package net.moisesborges.db

import androidx.room.Query
import androidx.room.Dao
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Insert
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface StationDao {

    @Insert
    fun insertStation(stationEntity: StationEntity)

    @Update
    fun updateStation(stationEntity: StationEntity)

    @Delete
    fun deleteStation(stationEntity: StationEntity)

    @Query("SELECT * FROM station WHERE id = (:stationId) ORDER BY created_at DESC")
    fun fetchStation(stationId: Int): Maybe<StationEntity>

    @Query("SELECT * FROM station")
    fun fetchAllStations(): Flowable<List<StationEntity>>
}