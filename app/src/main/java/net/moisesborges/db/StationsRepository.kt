package net.moisesborges.db

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import net.moisesborges.model.Genre
import net.moisesborges.model.Image
import net.moisesborges.model.Station
import net.moisesborges.model.Stream
import java.util.Date

class StationsRepository(private val stationDao: StationDao) {

    fun saveStation(station: Station): Completable {
        return Completable.fromAction {
            val stationEntity = stationToEntity(station)
            stationEntity.createdAt = Date()
            stationDao.insertStation(stationEntity)
        }
    }

    fun removeStation(station: Station): Completable {
        return Completable.fromAction {
            val stationEntity = stationToEntity(station)
            stationDao.deleteStation(stationEntity)
        }
    }

    fun getAllStations(): Observable<List<Station>> {
        return stationDao.fetchAllStations()
            .map { it.map(this::entityToStation) }
            .toObservable()
    }

    /**
     * Return a single with the station or Station.EMPTY_STATION otherwise
     */
    fun getStation(stationId: Int): Single<Station> {
        return stationDao.fetchStation(stationId)
            .map(this::entityToStation)
            .defaultIfEmpty(Station.EMPTY_STATION)
            .toSingle()
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
            station.stream?.bitrate,
            null
        )
    }

    private fun entityToStation(stationEntity: StationEntity): Station {
        val streamUrl = stationEntity.streamUrl
        val stream = if (streamUrl != null) Stream(
            streamUrl,
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