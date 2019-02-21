package net.moisesborges.ui.audio.mvvm

import net.moisesborges.model.Station
import net.moisesborges.ui.audio.player.PlaybackState

data class AudioPlayerState(
    val station: Station,
    val playbackState: PlaybackState
)

fun initialAudioPlayerState(station: Station) = AudioPlayerState(station, PlaybackState(false))