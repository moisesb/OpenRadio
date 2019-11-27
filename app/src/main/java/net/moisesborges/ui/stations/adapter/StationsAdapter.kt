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

package net.moisesborges.ui.stations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import net.moisesborges.R
import net.moisesborges.databinding.StationItemBinding
import net.moisesborges.model.Station
import net.moisesborges.ui.base.LifecycleRecyclerViewAdapter
import net.moisesborges.ui.base.LifecycleViewHolder
import net.moisesborges.ui.base.StationsDiffCallback
import net.moisesborges.ui.home.adapter.StationItemViewModelFactory

class StationsAdapter(
    private val viewModelFactory: StationItemViewModelFactory
) : LifecycleRecyclerViewAdapter<StationsViewHolder>() {

    var stations: List<Station> = emptyList()
    set(value) {
        val oldStations = field
        field = value
        val diffResult = DiffUtil.calculateDiff(StationsDiffCallback(oldStations, value))
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    override fun onBindViewHolder(holder: StationsViewHolder, position: Int) {
        val station = stations[position]
        holder.bind(station)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: StationItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.station_item, parent, false)
        return StationsViewHolder(binding, viewModelFactory)
    }
}

class StationsViewHolder(
    private val binding: StationItemBinding,
    private val viewModelFactory: StationItemViewModelFactory
) : LifecycleViewHolder(binding) {

    fun bind(station: Station) {
        binding.viewModel = viewModelFactory.create(station)
    }
}