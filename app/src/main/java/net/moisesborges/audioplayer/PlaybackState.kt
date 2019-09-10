package net.moisesborges.audioplayer

import net.moisesborges.model.Station

data class PlaybackState(val station: Station, val isPlaying: Boolean)

fun initialPlaybackState() = PlaybackState(Station.EMPTY_STATION, false)