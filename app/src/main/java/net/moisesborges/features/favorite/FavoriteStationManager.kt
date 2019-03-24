package net.moisesborges.features.favorite

import io.reactivex.Single
import net.moisesborges.db.StationsRepository
import net.moisesborges.model.Station

class FavoriteStationManager(private val stationsRepository: StationsRepository) {

    fun favoriteState(stationId: Int): Single<Boolean> {
        return stationsRepository.getStation(stationId)
            .map { it != Station.EMPTY_STATION }
    }

    fun toggleState(station: Station): Single<Boolean> {
        return favoriteState(station.id)
            .flatMap { saved ->
                val completable = if (saved) {
                    stationsRepository.removeStation(station)
                } else {
                    stationsRepository.saveStation(station)
                }
                completable.andThen(favoriteState(station.id))
            }
    }
}