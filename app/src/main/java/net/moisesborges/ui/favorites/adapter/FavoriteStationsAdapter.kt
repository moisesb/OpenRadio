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

package net.moisesborges.ui.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import net.moisesborges.R
import net.moisesborges.databinding.FavoriteStationItemBinding
import net.moisesborges.model.Station
import net.moisesborges.ui.base.LifecycleRecyclerViewAdapter
import net.moisesborges.ui.base.LifecycleViewHolder
import net.moisesborges.ui.base.StationsDiffCallback

class FavoriteStationsAdapter(
    private val viewModelFactory: FavoriteStationItemViewModelFactory
) : LifecycleRecyclerViewAdapter<FavoriteStationViewHolder>() {

    private var favoriteStations: List<Station> = emptyList()
    private var oldFavoriteStations: List<Station> = emptyList()

    fun setStations(stations: List<Station>) {
        this.oldFavoriteStations = this.favoriteStations
        this.favoriteStations = stations
        val diffResult = DiffUtil.calculateDiff(StationsDiffCallback(this.oldFavoriteStations, this.favoriteStations))
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteStationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: FavoriteStationItemBinding = DataBindingUtil.inflate(inflater, R.layout.favorite_station_item, parent, false)
        return FavoriteStationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favoriteStations.size
    }

    override fun onBindViewHolder(holder: FavoriteStationViewHolder, position: Int) {
        val viewModel = viewModelFactory.create(favoriteStations[position])
        holder.bind(viewModel)
    }
}

class FavoriteStationViewHolder(
    private val binding: FavoriteStationItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: FavoriteStationItemViewModel) {
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }
}