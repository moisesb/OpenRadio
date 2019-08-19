package net.moisesborges.ui.station

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import net.moisesborges.R
import net.moisesborges.databinding.ActivityStationBinding
import net.moisesborges.model.Station
import net.moisesborges.ui.audioplayer.AudioPlayerViewModel
import net.moisesborges.ui.station.mvvm.StationViewModel
import net.moisesborges.ui.base.LifecycleActivity
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

private const val STATION_ARG = "StationActivity.station"
private const val STATION_ID_ARG = "StationActivity.stationId"

// TODO: fetch station details if only stationId is provided
class StationActivity : LifecycleActivity() {

    private lateinit var binding: ActivityStationBinding

    private val station: Station by lazy { intent.getParcelableExtra<Station>(STATION_ARG) }
    private val stationViewModel: StationViewModel by inject { parametersOf(station) }
    private val audioPlayerViewModel: AudioPlayerViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_station)
        binding.lifecycleOwner = this
        binding.stationViewModel = stationViewModel
        binding.audioPlayerViewModel = audioPlayerViewModel

        audioPlayerViewModel.prepareNextStation(station)
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerViewModel.clear()
    }
}

fun createStationActivityIntent(station: Station, context: Context) =
    Intent(context, StationActivity::class.java).apply {
        putExtra(STATION_ARG, station)
    }

fun createStationActivityIntent(stationId: Int, context: Context) =
    Intent(context, StationActivity::class.java).apply {
        putExtra(STATION_ID_ARG, stationId)
    }