package net.moisesborges.features.favorite

import io.reactivex.Observable
import net.moisesborges.db.StationsRepository
import net.moisesborges.model.Station

class FavoriteStationManager(private val stationsRepository: StationsRepository) {

    fun favoriteState(stationId: Int): Observable<Boolean> {
        return stationsRepository.getStation(stationId)
            .map { it != Station.EMPTY_STATION }
    }

    fun toggleState(station: Station) {
        favoriteState(station.id)
            .firstOrError()
            .subscribe { saved ->
                if (saved) {
                    stationsRepository.removeStation(station)
                } else {
                    stationsRepository.saveStation(station)
                }
            }
    }
}