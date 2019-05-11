package net.moisesborges.ui.top.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import net.moisesborges.R
import net.moisesborges.databinding.TopStationItemBinding
import net.moisesborges.model.Station
import net.moisesborges.ui.base.LifecycleRecyclerViewAdapter
import net.moisesborges.ui.base.LifecycleViewHolder
import net.moisesborges.ui.base.StationsDiffCallback
import net.moisesborges.ui.top.mvvm.PaginationDetector

class TopStationsAdapter(
    private val topStationItemViewModelFactory: TopStationItemViewModelFactory,
    private val paginationDetector: PaginationDetector
) : LifecycleRecyclerViewAdapter<StationViewHolder>() {

    private var stations = listOf<Station>()
    private var oldStations = listOf<Station>()

    fun setStations(stations: List<Station>) {
        this.oldStations = this.stations
        this.stations = stations
        val diffResult = DiffUtil.calculateDiff(StationsDiffCallback(this.oldStations, stations))
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TopStationItemBinding>(inflater, R.layout.top_station_item,
            parent, false)
        return StationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stations[position]
        holder.bind(topStationItemViewModelFactory.create(station))
        paginationDetector.onItemDisplayed(position, stations.size)
    }
}

class StationViewHolder(
    private val binding: TopStationItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: TopStationItemViewModel) {
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }
}