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