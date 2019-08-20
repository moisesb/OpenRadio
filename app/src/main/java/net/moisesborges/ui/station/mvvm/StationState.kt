package net.moisesborges.ui.station.mvvm

import net.moisesborges.model.Station
import net.moisesborges.audioplayer.PlaybackState

data class StationState(
    val station: Station,
    val playbackState: PlaybackState
)

fun initialStationState(station: Station) = StationState(station, PlaybackState(isPlaying = false, isLoaded = false))