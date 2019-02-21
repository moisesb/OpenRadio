package net.moisesborges.ui.top.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.moisesborges.R
import net.moisesborges.databinding.TopStationItemBinding
import net.moisesborges.model.Station

class TopStationsAdapter(
    private val topStationItemViewModelFactory: TopStationItemViewModelFactory
) : RecyclerView.Adapter<StationViewHolder>() {

    private val stations = mutableListOf<Station>()

    fun setStations(stations: List<Station>) {
        this.stations.apply {
            clear()
            addAll(stations)
        }
        notifyDataSetChanged()
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
    }
}

class StationViewHolder(
    private val binding: TopStationItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: TopStationItemViewModel) {
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }
}