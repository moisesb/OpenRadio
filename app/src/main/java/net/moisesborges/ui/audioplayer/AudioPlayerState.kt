package net.moisesborges.ui.audioplayer

import net.moisesborges.audioplayer.PlaybackState
import net.moisesborges.model.Station

data class AudioPlayerState(
    val nextStation: Station?,
    val currentStation: Station,
    val playbackState: PlaybackState
)

fun initialAudioPlayerState() = AudioPlayerState(null, Station.EMPTY_STATION, PlaybackState(false))