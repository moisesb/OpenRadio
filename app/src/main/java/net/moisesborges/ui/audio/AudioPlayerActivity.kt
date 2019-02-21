package net.moisesborges.ui.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import net.moisesborges.R
import net.moisesborges.databinding.ActivityAudioPlayerBinding
import net.moisesborges.model.Station
import net.moisesborges.ui.audio.mvvm.AudioPlayerViewModel
import net.moisesborges.ui.base.LifecycleActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

private const val STATION_ARG = "AudioPlayerActivity.station"

class AudioPlayerActivity : LifecycleActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private val station: Station by lazy { intent.getParcelableExtra<Station>(STATION_ARG) }
    private val viewModel: AudioPlayerViewModel by inject { parametersOf(station) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_player)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}

fun createAudioPlayerActivityIntent(station: Station, context: Context) =
    Intent(context, AudioPlayerActivity::class.java).apply {
        putExtra(STATION_ARG, station)
    }