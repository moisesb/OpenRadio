package net.moisesborges.ui.station.mvvm

import net.moisesborges.model.Station
import net.moisesborges.audioplayer.PlaybackState
import net.moisesborges.audioplayer.initialPlaybackState

data class StationState(
    val station: Station,
    val playbackState: PlaybackState,
    val isLoadingStation: Boolean,
    val error: Error?
)

sealed class Error {
    object StationNotLoadedError : Error()
}

fun initialStationState() = StationState(Station.EMPTY_STATION, initialPlaybackState(), false, null)