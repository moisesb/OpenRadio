/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.moisesborges.ui.station

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import net.moisesborges.R
import net.moisesborges.databinding.ActivityStationBinding
import net.moisesborges.model.Station
import net.moisesborges.ui.station.mvvm.StationViewModel
import net.moisesborges.ui.base.LifecycleActivity
import org.koin.android.ext.android.inject

private const val STATION_ARG = "StationActivity.station"
private const val STATION_ID_ARG = "StationActivity.stationId"

class StationActivity : LifecycleActivity() {

    private lateinit var binding: ActivityStationBinding

    private val station: Station? by lazy { intent.getParcelableExtra<Station>(STATION_ARG) }
    private val stationId: Int by lazy { intent.getIntExtra(STATION_ID_ARG, 0) }
    private val viewModel: StationViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_station)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val station = station
        if (station != null) {
            viewModel.prepareStation(station)
        } else {
            viewModel.prepareStation(stationId)
        }
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
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