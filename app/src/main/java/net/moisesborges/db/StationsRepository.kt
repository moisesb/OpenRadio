package net.moisesborges.db

import io.reactivex.Completable
import io.reactivex.Observable
import net.moisesborges.model.Genre
import net.moisesborges.model.Image
import net.moisesborges.model.Station
import net.moisesborges.model.Stream

class StationsRepository(private val stationDao: StationDao) {

    fun saveStation(station: Station) {
        Completable.fromAction {
            val stationEntity = stationToEntity(station)
            stationDao.insertStation(stationEntity)
        }
    }

    fun removeStation(station: Station) {
        Completable.fromAction {
            val stationEntity = stationToEntity(station)
            stationDao.deleteStation(stationEntity)
        }
    }

    fun getAllStations(): Observable<List<Station>> {
        return stationDao.fetchAllStations()
            .map { it.map(this::entityToStation) }
            .toObservable()
    }

    fun getStation(stationId: Int): Observable<Station> {
        return stationDao.fetchStation(stationId)
            .map(this::entityToStation)
            .toObservable()
    }

    private fun stationToEntity(station: Station): StationEntity {
        return StationEntity(
            station.id,
            station.name,
            station.countryCode,
            station.image.url ?: "",
            station.thumbnail.url ?: "",
            station.genres.map { genre -> GenreEntity(genre.id, genre.title) },
            station.stream?.streamUrl,
            station.stream?.contentType,
            station.stream?.bitrate
        )
    }

    private fun entityToStation(stationEntity: StationEntity): Station {
        val stream = if (stationEntity.streamUrl != null) Stream(
            stationEntity.streamUrl,
            stationEntity.streamBitrate,
            stationEntity.streamContentType ?: ""
        ) else null
        return Station(
            stationEntity.id,
            stationEntity.name,
            stationEntity.countryCode,
            Image(stationEntity.imageUrl),
            Image(stationEntity.thumbnailUrl),
            stationEntity.genres.map { genreEntity -> Genre(genreEntity.id, genreEntity.name) },
            stream
        )
    }
}