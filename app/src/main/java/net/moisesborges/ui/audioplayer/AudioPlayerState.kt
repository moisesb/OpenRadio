package net.moisesborges.ui.audioplayer

import net.moisesborges.audioplayer.PlaybackState
import net.moisesborges.model.Station
import net.moisesborges.model.emptyStation

data class AudioPlayerState(
    val nextStation: Station?,
    val currentStation: Station,
    val playbackState: PlaybackState
)

fun initialAudioPlayerState() = AudioPlayerState(null, emptyStation(), PlaybackState(false))