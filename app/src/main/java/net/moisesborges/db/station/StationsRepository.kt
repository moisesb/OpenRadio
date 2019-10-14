/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.moisesborges.db.station

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import net.moisesborges.model.Genre
import net.moisesborges.model.ImageUrl
import net.moisesborges.model.Station
import net.moisesborges.model.StreamUrl
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
            .toSingle(Station.EMPTY_STATION)
    }

    private fun stationToEntity(station: Station): StationEntity {
        return StationEntity(
            station.id,
            station.name,
            station.countryCode,
            station.imageUrl.url,
            station.thumbnailUrl.url,
            station.genres.map { genre ->
                GenreEntity(
                    genre.id,
                    genre.title
                )
            },
            station.streamUrl.url,
            null
        )
    }

    private fun entityToStation(stationEntity: StationEntity): Station {
        val streamUrl = stationEntity.streamUrl
        val stream = StreamUrl(
            streamUrl
        )
        return Station(
            stationEntity.id,
            stationEntity.name,
            stationEntity.countryCode,
            ImageUrl(stationEntity.imageUrl),
            ImageUrl(stationEntity.thumbnailUrl),
            stationEntity.genres.map { genreEntity -> Genre(genreEntity.id, genreEntity.name) },
            stream
        )
    }
}